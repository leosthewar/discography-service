package com.clara.discographyservice.application.domain.model.release.responsemodel;


public record ReleaseImageResModel(Long id, String type, String discogsUri, String discogsResourceUrl, String discogsUri150,
                                   Integer width, Integer height) {
}
