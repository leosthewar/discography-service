package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.domain.model.Artist;

public record ImportArtistResponse(Artist artist, boolean imported) {

}
