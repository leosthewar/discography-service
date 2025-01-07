package com.clara.discographyservice.application.domain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArtistDeserializer extends JsonDeserializer<Artist> {

    public static final String FIELD_RESOURCE_URL = "resource_url";
    @Override
    public Artist deserialize(JsonParser parser, DeserializationContext context) throws IOException{
        JsonNode rootNode = parser.getCodec().readTree(parser);

        Long id = getLong(rootNode, "id");
        String name = getString(rootNode, "name");
        String resourceUrl = getString(rootNode, FIELD_RESOURCE_URL);
        String uri = getString(rootNode, "uri");
        String releasesUrl = getString(rootNode, "releases_url");
        String profile = getString(rootNode, "profile");
        String dataQuality = getString(rootNode, "data_quality");

        Set<String> nameVariations = getStringSet(rootNode, "namevariations");
        List<ArtistImage> images = getArtistImages(rootNode, "images");
        Set<String> urls = getStringSet(rootNode, "urls");
        List<ArtistMember> members = getArtistMembers(rootNode, "members");

        return new Artist(
                id, name, resourceUrl, uri, releasesUrl, profile,
                nameVariations, dataQuality, images, urls, members
        );
    }

    private Long getLong(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) ? node.get(fieldName).asLong() : null;
    }

    private String getString(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) ? node.get(fieldName).asText() : "";
    }

    private Set<String> getStringSet(JsonNode node, String fieldName) {
        Set<String> resultSet = new HashSet<>();
        if (node.hasNonNull(fieldName)) {
            node.get(fieldName).forEach(item -> resultSet.add(item.asText()));
        }
        return resultSet;
    }

    private List<ArtistImage> getArtistImages(JsonNode rootNode, String fieldName) {
        List<ArtistImage> images = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(imageNode -> images.add(new ArtistImage(
                    getString(imageNode, "type"),
                    getString(imageNode, "uri"),
                    getString(imageNode, FIELD_RESOURCE_URL),
                    getString(imageNode, "uri150"),
                    imageNode.hasNonNull("width") ? imageNode.get("width").asInt() : 0,
                    imageNode.hasNonNull("height") ? imageNode.get("height").asInt() : 0
            )));
        }
        return images;
    }

    private List<ArtistMember> getArtistMembers(JsonNode rootNode, String fieldName) {
        List<ArtistMember> members = new ArrayList<>();
        if (rootNode.hasNonNull(fieldName)) {
            rootNode.get(fieldName).forEach(memberNode -> members.add(new ArtistMember(
                    getLong(memberNode, "id"),
                    getString(memberNode, "name"),
                    getString(memberNode, FIELD_RESOURCE_URL),
                    memberNode.hasNonNull("active") && memberNode.get("active").asBoolean(),
                    getString(memberNode, "thumbnail_url")
            )));
        }
        return members;
    }
}
