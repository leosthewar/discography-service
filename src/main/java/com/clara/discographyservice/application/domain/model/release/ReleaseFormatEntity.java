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
@Table(name = "release_format")
public class ReleaseFormatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String qty;

    /**
     * Constructor to create a ReleaseFormatEntity with null id,
     * to use just to create a ReleaseFormatEntity before save in persistence
     */
    public ReleaseFormatEntity(String name, String qty) {
        this.name = name;
        this.qty = qty;
    }
}
