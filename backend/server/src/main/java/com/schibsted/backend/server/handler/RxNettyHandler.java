package com.schibsted.backend.server.handler;

import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.Endpoints;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;

import java.nio.charset.Charset;
import java.util.Optional;


public class RxNettyHandler implements RequestHandler<ByteBuf, ByteBuf> {


    private final Endpoints endpoints;

    public RxNettyHandler(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {

        Optional<Endpoint> endpoint = endpoints.findEndpoint(request.getUri(), request.getHttpMethod());

        if (endpoint.isPresent()) {
            return request.getContent().flatMap(byteBuf -> {
                String body = byteBuf.toString(Charset.defaultCharset());
                Request customRequest = new Request(request, request.getHttpMethod(), body);
                Response customResponse = endpoint.get().call(customRequest);
                response.setStatus(customResponse.getStatus());
                addCORSHeader(response);
                return response.writeStringAndFlush(customResponse.getBody());

            });

        } else {
            response.setStatus(HttpResponseStatus.BAD_REQUEST);
            addCORSHeader(response);
            return response.writeStringAndFlush("");

        }

    }

    private void addCORSHeader(HttpServerResponse<ByteBuf> response) {
        response.getHeaders().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080");
        response.getHeaders().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT");
        response.getHeaders().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type");
    }
}





