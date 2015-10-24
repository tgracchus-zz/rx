package com.schibsted.backend.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by ulises on 10/10/15.
 */
public class ResponseBuilder {

    private final ObjectMapper objectMapper;

    public ResponseBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public <T> Response newResponse(HttpResponseStatus status, Request request, T response) {
        try {
            return new Response(status, request, objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            return new Response(HttpResponseStatus.INTERNAL_SERVER_ERROR, request, "");
        }
    }


}

