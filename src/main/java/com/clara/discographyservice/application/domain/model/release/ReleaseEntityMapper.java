package com.clara.discographyservice.application.domain.model.release;


import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseArtistResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseFormatResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseGenreResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseImageResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseLabelResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseStyleResModel;
import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseTrackResModel;

import java.util.List;

public class ReleaseEntityMapper {

    private ReleaseEntityMapper() {
    }

    public static ReleaseResModel toResModel(ReleaseEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ReleaseResModel(
                entity.getId(),
                entity.getDiscogsId(),
                entity.getStatus(),
                entity.getYear(),
                entity.getDiscogsResourceUrl(),
                entity.getDiscogsUri(),
                entity.getFormatQuantity(),
                entity.getTitle(),
                entity.getCountry(),
                entity.getReleased(),
                entity.getNotes(),
                entity.getDiscogsThumb(),
                entity.getEstimatedWeight(),
                entity.getBlockedFromSale(),
                toResModelArtists(entity.getArtists()),
                toResModelLabels(entity.getLabels()),
                toResModelFormats(entity.getFormats()),
                toResModelGenres(entity.getGenres()),
                toResModelStyles(entity.getStyles()),
                toResModelTracks(entity.getTracks()),
                toResModelImages(entity.getImages())
        );
    }


    private static List<ReleaseArtistResModel> toResModelArtists(List<ReleaseArtistEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseArtistResModel(
                        entity.getId(),
                        entity.getDiscogsArtistId(),
                        entity.getName(),
                        entity.getAnv(),
                        entity.getAjoin(),
                        entity.getRole(),
                        entity.getTracks(),
                        entity.getDiscogsResourceUrl(),
                        entity.getDiscogsThumbnailUrl()
                )).toList();
    }


    private static List<ReleaseLabelResModel> toResModelLabels(List<ReleaseLabelEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseLabelResModel(
                        entity.getId(),
                        entity.getDiscogsLabelId(),
                        entity.getName(),
                        entity.getCatno(),
                        entity.getEntityType(),
                        entity.getEntityTypeName(),
                        entity.getDiscogsResourceUrl()
                )).toList();
    }

    private static List<ReleaseFormatResModel> toResModelFormats(List<ReleaseFormatEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseFormatResModel(
                        entity.getId(),
                        entity.getName(),
                        entity.getQty()
                )).toList();
    }

    private static List<ReleaseGenreResModel> toResModelGenres(List<ReleaseGenreEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseGenreResModel(
                        entity.getId(),
                        entity.getName()
                )).toList();
    }

    private static List<ReleaseStyleResModel> toResModelStyles(List<ReleaseStyleEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseStyleResModel(
                        entity.getId(),
                        entity.getName()
                )).toList();
    }

    private static List<ReleaseTrackResModel> toResModelTracks(List<ReleaseTrackEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseTrackResModel(
                        entity.getId(),
                        entity.getPosition(),
                        entity.getType(),
                        entity.getTitle(),
                        entity.getDuration()
                )).toList();
    }

    private static List<ReleaseImageResModel> toResModelImages(List<ReleaseImageEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(entity ->
                new ReleaseImageResModel(
                        entity.getId(),
                        entity.getType(),
                        entity.getDiscogsUri(),
                        entity.getDiscogsResourceUrl(),
                        entity.getDiscogsUri150(),
                        entity.getWidth(),
                        entity.getHeight()
                )).toList();
    }
    
}
