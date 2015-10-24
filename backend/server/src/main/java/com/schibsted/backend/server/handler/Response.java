package com.schibsted.backend.server.handler;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by ulises on 7/10/15.
 */
public class Response {

    private final Request request;
    private final HttpResponseStatus status;
    private final String responseBody;

    public Response(HttpResponseStatus status, Request request, String responseBody) {
        this.request = request;
        this.status = status;
        this.responseBody = responseBody;
    }


    public String getBody() {
        return responseBody;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }
}
