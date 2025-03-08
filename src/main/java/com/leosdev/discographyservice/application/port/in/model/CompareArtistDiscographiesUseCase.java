package com.leosdev.discographyservice.application.port.in.model;

public interface CompareArtistDiscographiesUseCase {

    CompareDiscographiesResponse compareDicographiesByDiscogsArtistId(CompareDiscographiesByDiscogsAristIdCommand command);

}
