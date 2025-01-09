package com.clara.discographyservice.application.domain.model.artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist_image")
public class ArtistImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "discogs_uri", length = 1024)
    private String discogsUri;

    @Column(name = "discogs_resource_url", length = 1024)
    private String discogsResourceUrl;

    @Column(name = "discogs_uri150", length = 1024)
    private String discogsUri150;

    private Integer width;
    private Integer height;

    /**
     * Constructor to create an ArtistImageEntity with null id,
     * to use just to create an ArtistMemberEntity before save in persistence
     */
    public ArtistImageEntity(String type, String discogsUri, String discogsResourceUrl, String discogsUri150, Integer width, Integer height) {
        this.type = type;
        this.discogsUri = discogsUri;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri150 = discogsUri150;
        this.width = width;
        this.height = height;
    }
}
