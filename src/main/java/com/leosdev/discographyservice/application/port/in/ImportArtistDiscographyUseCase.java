package com.leosdev.discographyservice.application.port.in;

import com.leosdev.discographyservice.application.port.in.model.ArtistDiscographyImportCommand;
import com.leosdev.discographyservice.application.port.in.model.ImportArtistDiscographyResponse;
import com.leosdev.discographyservice.application.port.in.model.ValidatePendingImportsCommand;

public interface ImportArtistDiscographyUseCase {

    ImportArtistDiscographyResponse createArtistDiscographyImport(ArtistDiscographyImportCommand importCommand);

    void validateAndUpdatePendingReleasesForImport(ValidatePendingImportsCommand pedingReleasesCommand);

}
