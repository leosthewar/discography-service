package com.clara.discographyservice.application.domain.model.artist;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistImageResModel;
import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistMemberResModel;
import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;

import java.util.List;

public class ArtistEntityMapper {

    private ArtistEntityMapper() {

    }

    public static ArtistResModel toReponseModel(ArtistEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ArtistResModel(
                entity.getId(),
                entity.getDiscogsId(),
                entity.getName(),
                entity.getDiscogsResourceUrl(),
                entity.getDiscogsUri(),
                entity.getDiscogsReleasesUrl(),
                entity.getProfile(),
                entity.getNameVariations(),
                entity.getDataQuality(),
                entity.getUrls(),
                entity.getDiscographyImported(),
                toReponseModelImages(entity.getImages()),
                toResponseModelMembers(entity.getMembers())
        );
    }

    private static List<ArtistImageResModel> toReponseModelImages(List<ArtistImageEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                       .map(entity -> new ArtistImageResModel(
                               entity.getId(),
                               entity.getType(),
                               entity.getDiscogsUri(),
                               entity.getDiscogsResourceUrl(),
                               entity.getDiscogsUri150(),
                               entity.getWidth(),
                               entity.getHeight()
                       ))
                       .toList();
    }


    private static List<ArtistMemberResModel> toResponseModelMembers(List<ArtistMemberEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                       .map(entity -> new ArtistMemberResModel(
                               entity.getId(),
                               entity.getDiscogsArtistId(),
                               entity.getName(),
                               entity.getDiscogsResourceUrl(),
                               entity.getActive(),
                               entity.getThumbnailUrl()
                       ))
                       .toList();
    }

}