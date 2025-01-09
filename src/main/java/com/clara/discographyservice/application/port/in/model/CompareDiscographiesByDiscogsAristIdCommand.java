package com.clara.discographyservice.application.port.in.model;


import java.util.Set;

public record CompareDiscographiesByDiscogsAristIdCommand(
        Set<Long> discogsArtistIds) {

    public CompareDiscographiesByDiscogsAristIdCommand {
        if (discogsArtistIds == null || discogsArtistIds.isEmpty()) {
            throw new IllegalArgumentException("discogsArtistIds must not be empty");
        }

        if(discogsArtistIds.size() ==1  || discogsArtistIds.size() > 5){
            throw new IllegalArgumentException("Compare minimum 1  and up to 5 artists");
        }
    }
}
