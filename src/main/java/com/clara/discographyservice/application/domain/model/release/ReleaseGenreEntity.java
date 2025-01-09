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
@Table(name = "release_genre")
public class ReleaseGenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * Constructor to create a ReleaseGenreEntity with null id,
     * to use just to create a ReleaseGenreEntity before save in persistence
     */
    public ReleaseGenreEntity(String name) {
        this.name = name;
    }
}
