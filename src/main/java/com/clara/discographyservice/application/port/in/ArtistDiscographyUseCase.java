package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.port.in.model.GetArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.model.GetDiscographyByDiscogsArtistIdCommand;

public interface ArtistDiscographyUseCase {

    GetArtistDiscographyResponse findDiscographyByArtistIdSortByYear(GetDiscographyByDiscogsArtistIdCommand command);



}
