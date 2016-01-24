package com.backend.server.parser;

import com.backend.server.endpoint.RequestParser;
import com.backend.server.handler.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.server.endpoint.RequestParserException;

import java.io.IOException;

/**
 * Created by ulises on 24/10/15.
 */
public class JsonRequestParser<T> implements RequestParser<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public JsonRequestParser(ObjectMapper objectMapper, Class<T> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public T parse(Request request) throws RequestParserException {
        try {
            return objectMapper.readValue(request.getBody(), type);
        } catch (IOException e) {
            throw new RequestParserException("Bad Format", e);
        }
    }
}
