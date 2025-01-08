    package com.clara.discographyservice.application.domain.model.artist;

    import com.clara.discographyservice.infrastructure.util.JsonNodeUtil;
    import com.fasterxml.jackson.core.JsonParser;
    import com.fasterxml.jackson.databind.DeserializationContext;
    import com.fasterxml.jackson.databind.JsonDeserializer;
    import com.fasterxml.jackson.databind.JsonNode;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Set;

    import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getString;
    import static com.clara.discographyservice.infrastructure.util.JsonNodeUtil.getStringSet;

    public class ArtistDeserializer extends JsonDeserializer<ArtistEntity> {

        public static final String FIELD_RESOURCE_URL = "resource_url";
        @Override
        public ArtistEntity deserialize(JsonParser parser, DeserializationContext context) throws IOException{
            JsonNode rootNode = parser.getCodec().readTree(parser);

            Long id = JsonNodeUtil.getLong(rootNode,"id");
            String name = getString(rootNode, "name");
            String resourceUrl = getString(rootNode, FIELD_RESOURCE_URL);
            String uri = getString(rootNode, "uri");
            String releasesUrl = getString(rootNode, "releases_url");
            String profile = getString(rootNode, "profile");
            String dataQuality = getString(rootNode, "data_quality");

            Set<String> nameVariations = getStringSet(rootNode, "namevariations");
            List<ArtistImageEntity> images = getArtistImages(rootNode, "images");
            Set<String> urls = getStringSet(rootNode, "urls");
            List<ArtistMemberEntity> members = getArtistMembers(rootNode, "members");

            return new ArtistEntity(id, name, resourceUrl, uri, releasesUrl, profile, nameVariations, dataQuality, urls, images, members);
        }



        private List<ArtistImageEntity> getArtistImages(JsonNode rootNode, String fieldName) {
            List<ArtistImageEntity> images = new ArrayList<>();
            if (rootNode.hasNonNull(fieldName)) {
                rootNode.get(fieldName).forEach(imageNode -> images.add(new ArtistImageEntity(
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

        private List<ArtistMemberEntity> getArtistMembers(JsonNode rootNode, String fieldName) {
            List<ArtistMemberEntity> members = new ArrayList<>();
            if (rootNode.hasNonNull(fieldName)) {
                rootNode.get(fieldName).forEach(memberNode -> members.add(new ArtistMemberEntity(
                        JsonNodeUtil.getLong(memberNode, "id"),
                        getString(memberNode, "name"),
                        getString(memberNode, FIELD_RESOURCE_URL),
                        memberNode.hasNonNull("active") && memberNode.get("active").asBoolean(),
                        getString(memberNode, "thumbnail_url")
                )));
            }
            return members;
        }
    }
