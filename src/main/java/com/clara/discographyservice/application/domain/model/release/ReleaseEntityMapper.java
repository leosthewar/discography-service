package com.clara.discographyservice.application.domain.model.release;


import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseResModel;

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
                entity.getBlockedFromSale()
        );
    }


}
