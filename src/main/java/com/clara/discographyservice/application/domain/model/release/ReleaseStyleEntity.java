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
@Table(name = "release_style")
public class ReleaseStyleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * Constructor to create a ReleaseStyleEntity with null id,
     * to use just to create a ReleaseStyleEntity before save in persistence
     */
    public ReleaseStyleEntity(String name) {
        this.name = name;
    }
}
