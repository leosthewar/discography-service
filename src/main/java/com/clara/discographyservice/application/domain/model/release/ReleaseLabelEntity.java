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


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "release_label")
@EqualsAndHashCode
public class ReleaseLabelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_label_id", nullable = false)
    private Long discogsLabelId;

    private String name;

    private String catno;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "entity_type_name")
    private String entityTypeName;

    @Column(name = "discogs_resource_url", length = 1024)
    private String discogsResourceUrl;

    /**
     * Constructor to create a ReleaseLabelEntity with null id,
     * to use just to create a ReleaseLabelEntity before save in persistence
     */
    public ReleaseLabelEntity(Long discogsLabelId, String name, String catno,
                              String entityType, String entityTypeName,
                              String discogsResourceUrl) {
        this.discogsLabelId = discogsLabelId;
        this.name = name;
        this.catno = catno;
        this.entityType = entityType;
        this.entityTypeName = entityTypeName;
        this.discogsResourceUrl = discogsResourceUrl;
    }
}
