package com.leosdev.discographyservice.application.port.in.model;

import com.leosdev.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;

public record ImportArtistResponse(ArtistResModel artist, boolean imported) {

}
