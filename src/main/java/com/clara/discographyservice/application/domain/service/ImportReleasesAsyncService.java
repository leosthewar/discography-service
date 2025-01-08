package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.domain.model.artist.ImportDetailsProjection;
import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import com.clara.discographyservice.application.port.in.DeserializerDiscogsResponseException;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.ImportReleasesAsyncUseCase;
import com.clara.discographyservice.application.port.in.ReleaseGetQuery;
import com.clara.discographyservice.application.port.in.ValidatePendingImportsCommand;
import com.clara.discographyservice.application.port.out.ArtistDiscographyImportRepository;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.clara.discographyservice.application.port.out.ReleaseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class ImportReleasesAsyncService implements ImportReleasesAsyncUseCase {


    private static final Logger logger = LoggerFactory.getLogger(ImportReleasesAsyncService.class);

    private final DiscogsAPIClient discogsAPIClient;
    private final ArtistDiscographyImportRepository artistDiscographyImportRepository;
    private final ReleaseRepository releaseRepository;
    private final ObjectMapper objectMapper;
    private final ImportArtistDiscographyUseCase importArtistDiscographyUseCase;


    public ImportReleasesAsyncService(DiscogsAPIClient discogsAPIClient,
                                      ArtistDiscographyImportRepository artistDiscographyImportRepository,
                                      ReleaseRepository releaseRepository, ObjectMapper objectMapper,
                                      ImportArtistDiscographyUseCase importArtistDiscographyUseCase) {
        this.discogsAPIClient = discogsAPIClient;
        this.artistDiscographyImportRepository = artistDiscographyImportRepository;
        this.releaseRepository = releaseRepository;
        this.objectMapper = objectMapper;
        this.importArtistDiscographyUseCase=importArtistDiscographyUseCase;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeImportation() {
        List<ImportDetailsProjection> releasesToImport = artistDiscographyImportRepository.findNotImportedReleases();

        if (releasesToImport == null || releasesToImport.isEmpty()) {
            return;
        }
        importReleases(releasesToImport);
        List<Long> artistIds = releasesToImport.stream()
                                               .map(ImportDetailsProjection::getImportId)
                                               .distinct()
                                               .toList();
        importArtistDiscographyUseCase.validateAndUpdatePendingReleasesForImport(new ValidatePendingImportsCommand(artistIds));
    }

    private void importReleases(List<ImportDetailsProjection> releasesToImport) {
        releasesToImport.forEach(importDetails -> {
            discogsAPIClient.getRelease(new ReleaseGetQuery(importDetails.getDiscogsReleaseId()))
                            .ifPresent(releaseResponse -> processRelease(importDetails, releaseResponse));
        });
    }

    private void processRelease(ImportDetailsProjection importDetails, JsonNode releaseResponse) {
        try {
            ReleaseEntity release = objectMapper.treeToValue(releaseResponse, ReleaseEntity.class);
            release = releaseRepository.save(release);

            logger.info("updating detailId: {}", importDetails.getImportDetailId());
            artistDiscographyImportRepository.setImportDetailTrue(importDetails.getImportDetailId());
            logger.info("Release: {}", release);
        } catch (JsonProcessingException e) {
            throw new DeserializerDiscogsResponseException("Error deserializing Discogs response", e);
        }
    }




}
