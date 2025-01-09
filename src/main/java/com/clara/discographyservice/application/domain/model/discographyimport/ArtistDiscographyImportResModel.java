package com.clara.discographyservice.application.domain.model.discographyimport;


import java.time.LocalDateTime;

public record ArtistDiscographyImportResModel(Long id, Long artistId, Long discogsArtistId,
                                              ArtistDiscographyImportStatusEnum status, LocalDateTime createdAt,
                                              LocalDateTime finishedAt, Integer releasesTotal) {

}
