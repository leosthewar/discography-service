package com.clara.discographyservice.application.domain.model.discographyimport;


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

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist_discography_import_detail")
public class ArtistDiscographyImportDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_release_id", nullable = false)
    private Long discogsReleaseId;

    private Boolean imported;

    public ArtistDiscographyImportDetailEntity(Long discogsReleaseId, Boolean imported) {
        this.discogsReleaseId = discogsReleaseId;
        this.imported = imported;
    }
}