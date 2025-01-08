package com.clara.discographyservice.application.domain.model.release;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getBoolean;
import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getInt;
import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getLong;
import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getString;

public class ReleaseDeserializer extends JsonDeserializer<ReleaseEntity> {

    private static final String FIELD_RESOURCE_URL = "resource_url";

    @Override
    public ReleaseEntity deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode rootNode = parser.getCodec().readTree(parser);

        Long discogsId = getLong(rootNode, "id");
        String status = getString(rootNode, "status");
        Integer year = getInt(rootNode, "year");
        String resourceUrl = getString(rootNode, FIELD_RESOURCE_URL);
        String uri = getString(rootNode, "uri");
        Integer formatQuantity = getInt(rootNode, "format_quantity");
        String title = getString(rootNode, "title");
        String country = getString(rootNode, "country");
        String released = getString(rootNode, "released");
        String notes = getString(rootNode, "notes");
        String thumb = getString(rootNode, "thumb");
        Integer estimatedWeight = getInt(rootNode, "estimated_weight");
        Boolean blockedFromSale = getBoolean(rootNode, "blocked_from_sale");

        List<ReleaseArtistEntity> artists = getReleaseArtists(rootNode, "artists");
        List<ReleaseLabelEntity> labels = getReleaseLabels(rootNode, "labels");
        List<ReleaseFormatEntity> formats = getReleaseFormats(rootNode, "formats");
        List<ReleaseGenreEntity> genres = getStringListAsReleaseGenres(rootNode, "genres");
        List<ReleaseStyleEntity> styles = getStringListAsReleaseStyles(rootNode, "styles");
        List<ReleaseTrackEntity> tracks = getReleaseTracks(rootNode, "tracklist");
        List<ReleaseImageEntity> images = getReleaseImages(rootNode, "images");

        return new ReleaseEntity(discogsId, status, year, resourceUrl, uri, formatQuantity, title, country,
                released, notes, thumb, estimatedWeight, blockedFromSale, artists, labels, formats, genres,
                styles, tracks, images);
    }


    private List<ReleaseArtistEntity> getReleaseArtists(JsonNode rootNode, String fieldName) {
        List<ReleaseArtistEntity> artists = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(artistNode -> artists.add(new ReleaseArtistEntity(
                    getLong(artistNode, "id"),
                    getString(artistNode, "name"),
                    getString(artistNode, "anv"),
                    getString(artistNode, "join"),
                    getString(artistNode, "role"),
                    getString(artistNode, "tracks"),
                    getString(artistNode, FIELD_RESOURCE_URL),
                    getString(artistNode, "thumbnail_url")
            )));
        }
        return artists;
    }

    private List<ReleaseLabelEntity> getReleaseLabels(JsonNode rootNode, String fieldName) {
        List<ReleaseLabelEntity> labels = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(labelNode -> labels.add(new ReleaseLabelEntity(
                    getLong(labelNode, "id"),
                    getString(labelNode, "name"),
                    getString(labelNode, "catno"),
                    getString(labelNode, "entity_type"),
                    getString(labelNode, "entity_type_name"),
                    getString(labelNode, FIELD_RESOURCE_URL)
            )));
        }
        return labels;
    }

    private List<ReleaseFormatEntity> getReleaseFormats(JsonNode rootNode, String fieldName) {
        List<ReleaseFormatEntity> formats = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(formatNode -> formats.add(new ReleaseFormatEntity(
                    getString(formatNode, "name"),
                    getString(formatNode, "qty")
            )));
        }
        return formats;
    }

    private List<ReleaseGenreEntity> getStringListAsReleaseGenres(JsonNode rootNode, String fieldName) {
        List<ReleaseGenreEntity> genres = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(genreNode -> genres.add(new ReleaseGenreEntity( genreNode.asText())));
        }
        return genres;
    }

    private List<ReleaseStyleEntity> getStringListAsReleaseStyles(JsonNode rootNode, String fieldName) {
        List<ReleaseStyleEntity> styles = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(styleNode -> styles.add(new ReleaseStyleEntity( styleNode.asText())));
        }
        return styles;
    }

    private List<ReleaseTrackEntity> getReleaseTracks(JsonNode rootNode, String fieldName) {
        List<ReleaseTrackEntity> tracks = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(trackNode -> tracks.add(new ReleaseTrackEntity(
                    getString(trackNode, "position"),
                    getString(trackNode, "type_"),
                    getString(trackNode, "title"),
                    getString(trackNode, "duration")
            )));
        }
        return tracks;
    }

    private List<ReleaseImageEntity> getReleaseImages(JsonNode rootNode, String fieldName) {
        List<ReleaseImageEntity> images = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(imageNode -> images.add(new ReleaseImageEntity(
                    getString(imageNode, "type"),
                    getString(imageNode, "uri"),
                    getString(imageNode, FIELD_RESOURCE_URL),
                    getString(imageNode, "uri150"),
                    getInt(imageNode, "width"),
                    getInt(imageNode, "height")
            )));
        }
        return images;
    }
}
