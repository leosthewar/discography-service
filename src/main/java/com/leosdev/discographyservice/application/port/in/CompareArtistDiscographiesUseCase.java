package com.leosdev.discographyservice.application.port.in;

import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesByDiscogsAristIdCommand;
import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesResponse;

public interface CompareArtistDiscographiesUseCase {

    CompareDiscographiesResponse compareDicographiesByDiscogsArtistId(CompareDiscographiesByDiscogsAristIdCommand command);

}
