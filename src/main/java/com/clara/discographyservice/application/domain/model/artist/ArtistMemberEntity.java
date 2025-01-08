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
@Table(name = "artist_member")
public class ArtistMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_artist_id", nullable = false)
    private Long discogsArtistId;

    private String name;

    @Column(name = "discogs_resource_url")
    private String discogsResourceUrl;

    private Boolean active;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    public ArtistMemberEntity(Long discogsArtistId, String name, String discogsResourceUrl, Boolean active, String thumbnailUrl) {
        this.discogsArtistId = discogsArtistId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.active = active;
        this.thumbnailUrl = thumbnailUrl;
    }
}
