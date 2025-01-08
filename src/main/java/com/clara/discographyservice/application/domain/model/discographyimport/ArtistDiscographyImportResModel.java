package com.clara.discographyservice.application.domain.model.discographyimport;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ArtistDiscographyImportResModel {

    private final Long id;
    private final Long artistId;
    private final Long discogsArtistId;
    private  final ArtistDiscographyImportStatusEnum status;
    private final LocalDateTime createdAt;
    private   final LocalDateTime finishedAt;
    private final Integer releasesTotal;

}
