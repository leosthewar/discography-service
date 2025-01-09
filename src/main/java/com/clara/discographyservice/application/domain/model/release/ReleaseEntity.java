package com.clara.discographyservice.application.domain.model.release;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "release")
public class ReleaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_id", nullable = false, unique = true)
    private Long discogsId;

    private String status;

    private Integer year;

    @Column(name = "discogs_resource_url", length = 1024)
    private String discogsResourceUrl;

    @Column(name = "discogs_uri", length = 1024)
    private String discogsUri;

    @Column(name = "format_quantity")
    private Integer formatQuantity;

    private String title;

    private String country;

    private String released;

    @Lob
    private String notes;

    @Column(name = "discogs_thumb", length = 1024)
    private String discogsThumb;

    @Column(name = "estimated_weight")
    private Integer estimatedWeight;

    @Column(name = "blocked_from_sale")
    private Boolean blockedFromSale;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseArtistEntity> artists;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseLabelEntity> labels;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseFormatEntity> formats;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseGenreEntity> genres;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseStyleEntity> styles;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseTrackEntity> tracks;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "release_id", nullable = false)
    private List<ReleaseImageEntity> images;

    /**
     * Constructor to create a ReleaseArtistEntity with null id,
     * to use just to create a ReleaseArtistEntity before save in persistence
     */
    public ReleaseEntity(Long discogsId, String status, Integer year,
                         String discogsResourceUrl, String discogsUri,
                         Integer formatQuantity, String title, String country,
                         String released, String notes, String discogsThumb,
                         Integer estimatedWeight, Boolean blockedFromSale,
                         List<ReleaseArtistEntity> artists,
                         List<ReleaseLabelEntity> labels,
                         List<ReleaseFormatEntity> formats,
                         List<ReleaseGenreEntity> genres,
                         List<ReleaseStyleEntity> styles,
                         List<ReleaseTrackEntity> tracks,
                         List<ReleaseImageEntity> images) {
        if (discogsId == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException("discogsId and title must not be null or empty");
        }
        this.discogsId = discogsId;
        this.status = status;
        this.year = year;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri = discogsUri;
        this.formatQuantity = formatQuantity;
        this.title = title;
        this.country = country;
        this.released = released;
        this.notes = notes;
        this.discogsThumb = discogsThumb;
        this.estimatedWeight = estimatedWeight;
        this.blockedFromSale = blockedFromSale;
        this.artists = artists;
        this.labels = labels;
        this.formats = formats;
        this.genres = genres;
        this.styles = styles;
        this.tracks = tracks;
        this.images = images;
    }
}
