package com.clara.discographyservice.application.domain.model.discographyimport;


public class ArtistDiscographyImportMapper {

    private ArtistDiscographyImportMapper() {

    }

    public static ArtistDiscographyImportResModel toResponseModel(ArtistDiscographyImportEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ArtistDiscographyImportResModel(
                entity.getId(),
                entity.getArtistId(),
                entity.getDiscogsArtistId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getFinishedAt(),
                entity.getReleasesTotal());

    }

}
