package com.backend.app.endpoints;

import com.backend.server.endpoint.Endpoint;
import com.backend.server.endpoint.RequestParser;
import com.backend.server.handler.Request;
import com.backend.server.handler.Response;
import com.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;

/**
 * Created by ulises on 23/01/16.
 */
public class BadRequestEndpoint extends Endpoint<Void> {

    public BadRequestEndpoint(String pathExpression, ResponseBuilder responseBuilder, HttpMethod httpMethod, RequestParser<Void> requestParser) {
        super(pathExpression, responseBuilder, httpMethod, requestParser);
    }

    @Override
    public Observable<Response> doCall(Request request, Void parsedRequest) throws Exception {
        return newResponse(HttpResponseStatus.BAD_REQUEST, request, new String(""));
    }
}
