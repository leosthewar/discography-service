package com.clara.discographyservice.adapter.in.rest;


import java.util.List;

public record CompareArtistDiscographiesRequestDTO(List<Long> discogsArtistIds) {
}
