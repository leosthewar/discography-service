package com.clara.discographyservice.application.domain.model.release;

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
@Table(name = "release_track")
public class ReleaseTrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private String type;

    private String title;

    private String duration;

    /**
     * Constructor to create a ReleaseTrackEntity with null id,
     * to use just to create a ReleaseTrackEntity before save in persistence
     */
    public ReleaseTrackEntity(String position, String type, String title, String duration) {
        this.position = position;
        this.type = type;
        this.title = title;
        this.duration = duration;
    }
}
