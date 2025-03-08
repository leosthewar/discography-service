package com.leosdev.discographyservice.application.port.in;

import com.leosdev.discographyservice.application.port.in.model.ArtistImportCommand;
import com.leosdev.discographyservice.application.port.in.model.ImportArtistResponse;

import java.util.Optional;

public interface ImportArtistUseCase {

    Optional<ImportArtistResponse> importArtist(ArtistImportCommand importCommand);

}
