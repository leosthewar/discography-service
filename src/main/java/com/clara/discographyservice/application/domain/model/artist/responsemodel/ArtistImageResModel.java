package com.clara.discographyservice.application.domain.model.artist.responsemodel;



public record ArtistImageResModel(Long id, String type, String discogsUri, String discogsResourceUrl,
                                  String discogsUri150, Integer width, Integer height) {

}
