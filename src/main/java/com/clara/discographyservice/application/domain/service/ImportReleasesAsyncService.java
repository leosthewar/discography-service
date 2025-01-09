package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.domain.model.artist.ImportDetailsProjection;
import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.ImportReleasesAsyncUseCase;
import com.clara.discographyservice.application.port.in.model.ReleaseGetQuery;
import com.clara.discographyservice.application.port.in.model.ValidatePendingImportsCommand;
import com.clara.discographyservice.application.port.out.ArtistDiscographyImportRepository;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.clara.discographyservice.application.port.out.DiscogsException;
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
import java.util.Optional;

@Service
class ImportReleasesAsyncService implements ImportReleasesAsyncUseCase {


    private static final Logger logger = LoggerFactory.getLogger(ImportReleasesAsyncService.class);

    private final DiscogsAPIClient discogsAPIClient;
    private final ArtistDiscographyImportRepository artistDiscographyImportRepository;
    private final ReleaseRepository releaseRepository;
    private final ObjectMapper objectMapper;
    private final ImportArtistDiscographyUseCase importArtistDiscographyImportUseCase;


    public ImportReleasesAsyncService(DiscogsAPIClient discogsAPIClient,
                                      ArtistDiscographyImportRepository artistDiscographyImportRepository,
                                      ReleaseRepository releaseRepository, ObjectMapper objectMapper,
                                      ImportArtistDiscographyUseCase importArtistDiscographyImportUseCase) {
        this.discogsAPIClient = discogsAPIClient;
        this.artistDiscographyImportRepository = artistDiscographyImportRepository;
        this.releaseRepository = releaseRepository;
        this.objectMapper = objectMapper;
        this.importArtistDiscographyImportUseCase = importArtistDiscographyImportUseCase;
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
        importArtistDiscographyImportUseCase.validateAndUpdatePendingReleasesForImport(new ValidatePendingImportsCommand(artistIds));
    }

    private void importReleases(List<ImportDetailsProjection> releasesToImport) {
        for (ImportDetailsProjection importDetails : releasesToImport) {
            try {
                Optional<JsonNode> discogsResponse = discogsAPIClient.getRelease(new ReleaseGetQuery(importDetails.getDiscogsReleaseId()));
                processRelease(importDetails, discogsResponse);
            } catch (DiscogsException e) {
                //TODO save logs in db
                // on DiscogsException, log and return without processing next releases,preventing possible Discogs API rate limit and guaranteeing data consistency
                logger.error("Discogs API error getting release: {}", importDetails.getDiscogsReleaseId(), e);
                return;
            }
        }
    }

    private void processRelease(ImportDetailsProjection importDetails, Optional<JsonNode> opReleaseResponse) {
        try {
            if (opReleaseResponse.isEmpty()) {
                // TODO save logs in db
                // some case release not found in Discogs
                logger.warn("release:{} not found in Discogs, delete the importDetail {}",importDetails.getDiscogsReleaseId(), importDetails.getImportDetailId());
                artistDiscographyImportRepository.deleteImportDetail(importDetails.getImportDetailId());
                return;
            }
            JsonNode releaseResponse = opReleaseResponse.get();
            ReleaseEntity release = objectMapper.treeToValue(releaseResponse, ReleaseEntity.class);
            if (releaseRepository.existsReleaseByDiscogsId(release.getDiscogsId())) {
                logger.debug("release exists, updating detailId: {}", importDetails.getImportDetailId());
                artistDiscographyImportRepository.setImportDetailTrue(importDetails.getImportDetailId());
                return;
            }
            release = releaseRepository.save(release);
            logger.debug("updating detailId: {}", importDetails.getImportDetailId());
            artistDiscographyImportRepository.setImportDetailTrue(importDetails.getImportDetailId());
            logger.debug("Release: {}", release);
        } catch (JsonProcessingException e) {
            // just log the error, execution will continue
            //TODO save logs in db
            logger.error("Error parsing release response : {}", e.getMessage());
        }
    }

}
