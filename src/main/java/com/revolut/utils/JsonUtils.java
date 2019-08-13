package com.revolut.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.ResponseTransformer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    public static final String EMPTY_RESPONSE = "";

    private static String toJson(Object data) {
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize (" + data + ") to JSON", e);
        }
    }

    public static ResponseTransformer json() {
        return JsonUtils::toJson;
    }

    public static JsonNode readJson(String data) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readTree(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON");
        }
    }
}