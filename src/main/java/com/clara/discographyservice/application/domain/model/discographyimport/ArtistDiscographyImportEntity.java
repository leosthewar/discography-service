package com.clara.discographyservice.application.domain.model.discographyimport;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist_discography_import")
public class ArtistDiscographyImportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_id", nullable = false)
    private Long artistId;

    @Column(name = "discogs_artist_id", nullable = false)
    private Long discogsArtistId;

    @Enumerated(EnumType.STRING) // Store as a string in the database
    @Column(nullable = false)
    private ArtistDiscographyImportStatusEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "releases_total")
    private Integer releasesTotal;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true )
    @JoinColumn(name = "artist_import_id", nullable = false)
    private List<ArtistDiscographyImportDetailEntity> details;


    /**
     * Constructor to create an ArtistDiscographyImportEntity with null id,
     * to use just to create an ArtistDiscographyImportEntity before save in persistence
     */
    public ArtistDiscographyImportEntity(Long artistId, Long discogsArtistId, ArtistDiscographyImportStatusEnum status, LocalDateTime createdAt, LocalDateTime finishedAt, Integer releasesTotal, List<ArtistDiscographyImportDetailEntity> details) {
        this.artistId = artistId;
        this.discogsArtistId = discogsArtistId;
        this.status = status;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.releasesTotal = releasesTotal;
        this.details = details;
    }

    public void setStatus(ArtistDiscographyImportStatusEnum status) {
        this.status = status;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}

