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
@Table(name = "release_image")
public class ReleaseImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(name = "discogs_uri")
    private String discogsUri;

    @Column(name = "discogs_resource_url")
    private String discogsResourceUrl;

    @Column(name = "discogs_uri150")
    private String discogsUri150;

    private Integer width;

    private Integer height;

    public ReleaseImageEntity(String type, String discogsUri, String discogsResourceUrl, String discogsUri150, Integer width, Integer height) {
        this.type = type;
        this.discogsUri = discogsUri;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri150 = discogsUri150;
        this.width = width;
        this.height = height;
    }
}
