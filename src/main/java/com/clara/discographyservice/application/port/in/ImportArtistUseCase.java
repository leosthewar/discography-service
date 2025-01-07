package com.clara.discographyservice.application.port.in;

import java.util.Optional;

public interface ImportArtistUseCase {

    Optional<ImportArtistResponse> importArtist(ArtistImportCommand saveCommand);

}
