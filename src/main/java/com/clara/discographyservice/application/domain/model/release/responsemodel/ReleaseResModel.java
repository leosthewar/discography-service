package com.clara.discographyservice.application.domain.model.release.responsemodel;


public record ReleaseResModel(Long id, Long discogsId, String status, Integer year, String discogsResourceUrl,
                              String discogsUri, Integer formatQuantity, String title, String country, String released,
                              String notes, String discogsThumb, Integer estimatedWeight, Boolean blockedFromSale
                             ) {

}
