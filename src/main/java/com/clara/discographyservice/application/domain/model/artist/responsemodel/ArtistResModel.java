package com.clara.discographyservice.application.domain.model.artist.responsemodel;

import java.util.List;
import java.util.Set;

public record ArtistResModel(Long id, Long discogsId, String name, String discogsResourceUrl, String discogsUri,
                             String discogsReleasesUrl, String profile, Set<String> nameVariations, String dataQuality,
                             Set<String> urls, Boolean discographyImported, List<ArtistImageResModel> images,
                             List<ArtistMemberResModel> members) {

}
