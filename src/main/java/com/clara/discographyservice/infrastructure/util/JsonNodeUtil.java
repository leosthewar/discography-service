package com.clara.discographyservice.infrastructure.util;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.Set;

public class JsonNodeUtil {

    private JsonNodeUtil() {}

    public static Long getLong(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) ? node.get(fieldName).asLong() : null;
    }

    public static String getString(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) ? node.get(fieldName).asText() : "";
    }

    public static Integer getInt(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) ? node.get(fieldName).asInt() : null;
    }

    public static Boolean getBoolean(JsonNode node, String fieldName) {
        return node.hasNonNull(fieldName) && node.get(fieldName).asBoolean();
    }

    public static Set<String> getStringSet(JsonNode node, String fieldName) {
        Set<String> resultSet = new HashSet<>();
        if (node.hasNonNull(fieldName)) {
            node.get(fieldName).forEach(item -> resultSet.add(item.asText()));
        }
        return resultSet;
    }
}

