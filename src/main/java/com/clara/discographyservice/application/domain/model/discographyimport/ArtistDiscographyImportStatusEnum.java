package com.clara.discographyservice.application.domain.model.discographyimport;


import java.util.HashMap;
import java.util.Map;

public enum ArtistDiscographyImportStatusEnum {
    CREATED("CREATED"),
    COMPLETED("COMPLETED");

    // Getter
    private final String code;

    // Constructor
    ArtistDiscographyImportStatusEnum(String code) {
        this.code = code;
    }

    // Static map for fast lookup
    private static final Map<String, ArtistDiscographyImportStatusEnum> CODE_TO_ENUM = new HashMap<>();

    static {
        for (ArtistDiscographyImportStatusEnum status : values()) {
            CODE_TO_ENUM.put(status.code.toUpperCase(), status);
        }
    }

    // Static method to safely retrieve an enum instance
    public static ArtistDiscographyImportStatusEnum fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Status code must not be null or empty");
        }
        ArtistDiscographyImportStatusEnum status = CODE_TO_ENUM.get(code.toUpperCase());
        if (status == null) {
            throw new IllegalArgumentException("No matching status for code: " + code);
        }
        return status;
    }
}
