package com.leosdev.discographyservice.application.port.in;

import com.leosdev.discographyservice.application.port.in.model.GetArtistDiscographyResponse;
import com.leosdev.discographyservice.application.port.in.model.GetDiscographyByDiscogsArtistIdCommand;

public interface ArtistDiscographyUseCase {

    GetArtistDiscographyResponse findDiscographyByArtistIdSortByYear(GetDiscographyByDiscogsArtistIdCommand command);



}
