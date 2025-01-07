package com.clara.discographyservice.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ArtistImage {

    private final Long id;
    private final String type;
    private final String discogsUri;
    private final String discogsResourceUrl;
    private final String discogsUri150;
    private final Integer width;
    private final Integer height;

    public ArtistImage(Long id, String type, String discogsUri, String discogsResourceUrl,
                       String discogsUri150, Integer width, Integer height) {

        if (id == null ) {
            throw new IllegalArgumentException("id must not be null.");
        }
        this.id = id;
        this.type = type;
        this.discogsUri = discogsUri;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri150 = discogsUri150;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor to create an ArtistImage with  null id
     *  It's use just to create an ArtistImage to save in persistence
     */
    public ArtistImage(String type, String discogsUri, String discogsResourceUrl, String discogsUri150, Integer width, Integer height) {
        this.id = null;
        this.type = type;
        this.discogsUri = discogsUri;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri150 = discogsUri150;
        this.width = width;
        this.height = height;
    }
}
