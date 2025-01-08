package com.clara.discographyservice.application.port.in;

public interface ImportArtistDiscographyUseCase {

    ImportArtistDiscographyResponse createArtistDiscographyImport(ArtistDiscographyImportCommand importCommand);

    void validateAndUpdatePendingReleasesForImport(ValidatePendingImportsCommand pedingReleasesCommand);

}
