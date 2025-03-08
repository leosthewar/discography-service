package com.leosdev.discographyservice.application.port.in.model;

import com.leosdev.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportResModel;

public record ImportArtistDiscographyResponse(
                                              String message,
                                              boolean created,
                                              boolean error,
                                              ArtistDiscographyImportResModel artistDiscographyImport) {

    public ImportArtistDiscographyResponse(String message, boolean created, boolean error ) {
        this(message, created, error, null);
    }



}
