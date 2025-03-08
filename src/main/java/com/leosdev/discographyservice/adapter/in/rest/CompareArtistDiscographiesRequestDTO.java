package com.leosdev.discographyservice.adapter.in.rest;


import java.util.List;

public record CompareArtistDiscographiesRequestDTO(List<Long> discogsArtistIds) {
}
