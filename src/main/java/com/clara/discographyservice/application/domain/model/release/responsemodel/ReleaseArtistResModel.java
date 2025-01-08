package com.clara.discographyservice.application.domain.model.release.responsemodel;


public record ReleaseArtistResModel(Long id, Long discogsArtistId, String name, String anv, String ajoin, String role,
                                    String tracks, String discogsResourceUrl, String discogsThumbnailUrl) {
}

