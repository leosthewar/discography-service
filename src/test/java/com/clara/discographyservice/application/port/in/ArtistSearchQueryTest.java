package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtistSearchQueryTest {

    @Test
    void testValidArtistSearchQuery() {
        // Given
         String query = "Nirvana";
         String currentURI= "http://domain.com/v1/discogs/artists/search";
         Integer page=1;
         Integer perPage=50;
         // when
        ArtistSearchQuery artistSearchQuery = new ArtistSearchQuery(query, currentURI, page, perPage);
         // then
        assertNotNull(artistSearchQuery);
        assertEquals(query, artistSearchQuery.query());
        assertEquals(currentURI, artistSearchQuery.currentURI());
        assertEquals(page, artistSearchQuery.page());
        assertEquals(perPage, artistSearchQuery.perPage());
    }

    @Test
    void testQueryNull(){

        // Given
        String currentURI= "http://domain.com/v1/discogs/artists/search";
        Integer page=1;
        Integer perPage=50;
        // when and then
        assertThrows(ConstraintViolationException.class, () -> new ArtistSearchQuery(null, currentURI, page, perPage));
    }
    @Test
    void testCurrentURILNull(){

        // Given
        String query = "Nirvana";
        Integer page=1;
        Integer perPage=50;
        // when and then
        assertThrows(ConstraintViolationException.class, () -> new ArtistSearchQuery(query, null, page, perPage));
    }

    @Test
    void testCurrentPaginationNull(){

        // Given
        String query = "Nirvana";
        String currentURI= "http://domain.com/v1/discogs/artists/search";
        // when
        ArtistSearchQuery artistSearchQuery = new ArtistSearchQuery(query, currentURI, null, null);
        // then
        assertNotNull(artistSearchQuery);
        assertEquals(1, artistSearchQuery.page());
        assertEquals(50, artistSearchQuery.perPage());
    }



}