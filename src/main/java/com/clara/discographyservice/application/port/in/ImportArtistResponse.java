package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;

public record ImportArtistResponse(ArtistResModel artist, boolean imported) {

}
