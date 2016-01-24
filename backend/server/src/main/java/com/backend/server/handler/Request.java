package com.backend.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;

/**
 * Created by ulises on 7/10/15.
 */
public class Request {

    private final HttpServerRequest<ByteBuf> request;

    private final HttpMethod method;
    private final String body;

    public Request(HttpServerRequest<ByteBuf> request, HttpMethod method, String body) {
        this.request = request;
        this.method = method;
        this.body = body;
    }

    public HttpServerRequest<ByteBuf> getRequest() {
        return request;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }
}
