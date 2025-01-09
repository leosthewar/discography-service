package com.clara.discographyservice.application.port.in.model;

import com.clara.discographyservice.application.domain.model.release.responsemodel.ReleaseResModel;
import org.springframework.data.domain.Page;

public record GetArtistDiscographyResponse(Page<ReleaseResModel> page,
                                           String message,
                                           boolean error) {


}
