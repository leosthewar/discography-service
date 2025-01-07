package com.clara.discographyservice.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ArtistMember {

    private final Long id;
    private final Long discogsArtistId;
    private final String name;
    private final String discogsResourceUrl;
    private final Boolean active;
    private final String thumbnailUrl;

    public ArtistMember(Long id, Long discogsArtistId,String name,
                        String discogsResourceUrl, Boolean active,String thumbnailUrl) {

        if (discogsArtistId == null) {
            throw new IllegalArgumentException("discogsArtistId must not be null.");
        }
        if (name == null) {
            throw new IllegalArgumentException("name must not be null.");
        }

        this.id = id;
        this.discogsArtistId = discogsArtistId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.active = active;
        this.thumbnailUrl=thumbnailUrl;
    }

    /**
     * Constructor to create an ArtistMember with  null id
     *  It's use just to create an ArtistMember to save in persistence
     */
    public ArtistMember(Long discogsArtistId, String name,
                        String discogsResourceUrl, Boolean active,String thumbnailUrl) {
        this.id= null;
        this.discogsArtistId = discogsArtistId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.active = active;
        this.thumbnailUrl=thumbnailUrl;
    }
}
