package com.epam.bank.clientinterface.controller.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static <T> String toJson(ObjectMapper objectMapper, T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(ObjectMapper objectMapper, String string, Class<T> clazz)
        throws JsonProcessingException {
        return objectMapper.readValue(string, clazz);
    }
}
