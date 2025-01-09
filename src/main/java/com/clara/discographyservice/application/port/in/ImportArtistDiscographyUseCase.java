package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.port.in.model.ArtistDiscographyImportCommand;
import com.clara.discographyservice.application.port.in.model.ImportArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.model.ValidatePendingImportsCommand;

public interface ImportArtistDiscographyUseCase {

    ImportArtistDiscographyResponse createArtistDiscographyImport(ArtistDiscographyImportCommand importCommand);

    void validateAndUpdatePendingReleasesForImport(ValidatePendingImportsCommand pedingReleasesCommand);

}
