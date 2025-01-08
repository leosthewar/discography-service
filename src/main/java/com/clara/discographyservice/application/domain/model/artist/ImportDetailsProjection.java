package com.clara.discographyservice.application.domain.model.artist;


public interface ImportDetailsProjection {

    Long getImportDetailId();
    Long getImportId();
    Long getDiscogsArtistId();
    Long getDiscogsReleaseId();
}