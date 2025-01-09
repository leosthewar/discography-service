package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportDetailEntity;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportEntity;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportMapper;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportResModel;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportStatusEnum;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistDiscographyImportCommand;
import com.clara.discographyservice.application.port.in.model.ArtistReleasesGetQuery;
import com.clara.discographyservice.application.port.in.model.ImportArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.model.ValidatePendingImportsCommand;
import com.clara.discographyservice.application.port.out.ArtistDiscographyImportRepository;
import com.clara.discographyservice.application.port.out.ArtistRepository;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
class ImportArtistDiscographyService implements ImportArtistDiscographyUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ImportArtistDiscographyService.class);

    private final DiscogsAPIClient discogsAPIClient;
    private final ArtistRepository artistRepository;
    private final ArtistDiscographyImportRepository artistDiscographyImportRepository;


    public ImportArtistDiscographyService(DiscogsAPIClient discogsAPIClient,
                                          ArtistRepository artistRepository,
                                          ArtistDiscographyImportRepository artistDiscographyImportRepository) {
        this.discogsAPIClient = discogsAPIClient;
        this.artistRepository = artistRepository;
        this.artistDiscographyImportRepository = artistDiscographyImportRepository;
    }

    @Override
    @Transactional
    public ImportArtistDiscographyResponse createArtistDiscographyImport(ArtistDiscographyImportCommand importCommand) {
        Optional<ArtistEntity> optionalArtist = artistRepository.findByDiscogsArtistId(importCommand.discogsArtistId());
        if (optionalArtist.isEmpty()) {
            return new ImportArtistDiscographyResponse("Artist has not been imported", false, true);
        }
        Optional<ArtistDiscographyImportEntity> currentImport = artistDiscographyImportRepository.findByDiscogsArtistId(importCommand.discogsArtistId());
        if (currentImport.isPresent()) {
            return new ImportArtistDiscographyResponse("Artist already has a discography importation", false, false, ArtistDiscographyImportMapper.toResponseModel(currentImport.get()));
        }

        Optional<JsonNode> optionalReleasesResponse = discogsAPIClient.getArtistReleases(
                new ArtistReleasesGetQuery(importCommand.discogsArtistId()));

        if (optionalReleasesResponse.isEmpty()) {
            return new ImportArtistDiscographyResponse("Artist does not have releases in Discogs", false, true);
        }

        JsonNode releasesResponse = optionalReleasesResponse.get();
        int totalItems = releasesResponse.get("pagination")
                                         .get("items")
                                         .asInt();
        int totalPages = releasesResponse.get("pagination")
                                         .get("pages")
                                         .asInt();

        if (totalItems > 500) {
            return new ImportArtistDiscographyResponse("Artist has more than 500 releases, currently this feature is limited to 500 releases. We are working on a feature to import more than 500 releases :)",
                    false, true);
        }

        List<ArtistDiscographyImportDetailEntity> allReleases = fetchAllArtistReleasesFromDiscogs(importCommand.discogsArtistId(), totalPages, releasesResponse);

        ArtistDiscographyImportEntity artistDiscographyImportEntity =
                new ArtistDiscographyImportEntity(
                optionalArtist.get()
                              .getId(),
                importCommand.discogsArtistId(),
                ArtistDiscographyImportStatusEnum.CREATED,
                LocalDateTime.now(),null,
                allReleases.size(),
                allReleases
        );


        artistDiscographyImportEntity = artistDiscographyImportRepository.save(artistDiscographyImportEntity);
        ArtistDiscographyImportResModel artistDiscographyImport = ArtistDiscographyImportMapper.toResponseModel(artistDiscographyImportEntity);
        logger.debug("Artist's discography importation has been created: {}", artistDiscographyImport.id());
        return new ImportArtistDiscographyResponse("Artist's discography importation has been created", true, false, artistDiscographyImport);
    }


    @Transactional
    public void validateAndUpdatePendingReleasesForImport(ValidatePendingImportsCommand pendingReleasesCommand) {
        pendingReleasesCommand.importIds()
                              .forEach(this::processImportId);
    }

    private void processImportId(Long importId) {
        int count = artistDiscographyImportRepository.countReleasesNotImportedByArtistId(importId);
        if (count == 0) {
            artistDiscographyImportRepository.findById(importId).ifPresent(this::updateImportAndArtist);
        }
    }

    private void updateImportAndArtist(ArtistDiscographyImportEntity importEntity) {
        importEntity.setStatus(ArtistDiscographyImportStatusEnum.COMPLETED);
        importEntity.setFinishedAt(LocalDateTime.now());
        artistDiscographyImportRepository.save(importEntity);

        artistRepository.findById(importEntity.getArtistId())
                        .ifPresent(artist -> {
                            artist.setDiscographyImported(true);
                            artistRepository.save(artist);
                        });
    }


    /**
     * Fetches all artist's releases from Discogs iterating over the pages
     *
     * @param discogsArtistId artist's discogs id
     * @param totalPages      total number of pages
     * @param initialResponse initial response
     * @return list of ArtistDiscographyImportDetail with all artist's releases
     */
    private List<ArtistDiscographyImportDetailEntity> fetchAllArtistReleasesFromDiscogs(long discogsArtistId, int totalPages, JsonNode initialResponse) {
        List<ArtistDiscographyImportDetailEntity> allReleases = extractReleaseIdsFromDiscogsResponse(initialResponse);

        for (int page = 2; page <= totalPages; page++) {
            Optional<JsonNode> pageResponse = discogsAPIClient.getArtistReleases(new ArtistReleasesGetQuery(discogsArtistId, page));
            pageResponse.ifPresent(response -> allReleases.addAll(extractReleaseIdsFromDiscogsResponse(response)));
        }

        return allReleases;
    }

    /**
     * Extracts release ids from Discogs response
     *
     * @param response response from Discogs , one specific page
     * @return an List of ArtistDiscographyImportDetail with all artist's releases in the response
     */
    private List<ArtistDiscographyImportDetailEntity> extractReleaseIdsFromDiscogsResponse(JsonNode response) {
        return StreamSupport.stream(response.get("releases")
                                            .spliterator(), false)
                            .map(release -> new ArtistDiscographyImportDetailEntity(release.get("id")
                                                                                     .asLong(), false))
                            .collect(Collectors.toList());
    }
}
