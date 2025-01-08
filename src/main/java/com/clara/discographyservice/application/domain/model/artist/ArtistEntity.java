package com.clara.discographyservice.application.domain.model.artist;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Set;


@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_id", nullable = false, unique = true)
    private Long discogsId;

    private String name;

    @Column(name = "discogs_resource_url")
    private String discogsResourceUrl;

    @Column(name = "discogs_uri")
    private String discogsUri;

    @Column(name = "discogs_releases_url")
    private String discogsReleasesUrl;

    @Column(name = "profile")
    private String profile;

    @Column(name = "name_variations")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> nameVariations;

    @Column(name = "data_quality")
    private String dataQuality;

    @Column(name = "urls")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> urls;

    @Column(name = "discography_imported")
    private Boolean discographyImported;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "artist_id",nullable = false)
    private List<ArtistImageEntity> images;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "artist_id",nullable = false)
    private List<ArtistMemberEntity> members;



    public ArtistEntity(Long discogsId, String name, String discogsResourceUrl,
                        String discogsUri, String discogsReleasesUrl,
                        String profile, Set<String> nameVariations, String dataQuality,
                        Set<String> urls,
                        List<ArtistImageEntity> images,
                        List<ArtistMemberEntity> members) {
        this.discogsId = discogsId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri = discogsUri;
        this.discogsReleasesUrl = discogsReleasesUrl;
        this.profile = profile;
        this.nameVariations = nameVariations;
        this.dataQuality = dataQuality;
        this.urls = urls;
        this.discographyImported = false;
        this.images = images;
        this.members = members;
    }

    public void setDiscographyImported(Boolean discographyImported) {
        this.discographyImported = discographyImported;
    }
}

