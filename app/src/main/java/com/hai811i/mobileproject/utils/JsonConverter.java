package com.hai811i.mobileproject.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonConverter {

    private static final Gson gson = new Gson();

    public static String convertToJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T parseJson(String json, Class<T> type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }
}