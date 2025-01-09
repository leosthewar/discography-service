package com.clara.discographyservice.application.domain.model.release;

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
@Table(name = "release_artist")
public class ReleaseArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_artist_id", nullable = false)
    private Long discogsArtistId;

    private String name;

    private String anv;

    private String ajoin;

    private String role;

    private String tracks;

    @Column(name = "discogs_resource_url", length = 1024)
    private String discogsResourceUrl;

    @Column(name = "discogs_thumbnail_url", length = 1024)
    private String discogsThumbnailUrl;

    /**
     * Constructor to create a ReleaseArtistEntity with null id,
     * to use just to create a ReleaseArtistEntity before save in persistence
     */
    public ReleaseArtistEntity(Long discogsArtistId, String name, String anv,
                               String ajoin, String role, String tracks,
                               String discogsResourceUrl, String discogsThumbnailUrl) {
        this.discogsArtistId = discogsArtistId;
        this.name = name;
        this.anv = anv;
        this.ajoin = ajoin;
        this.role = role;
        this.tracks = tracks;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsThumbnailUrl = discogsThumbnailUrl;
    }
}
