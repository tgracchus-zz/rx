package com.backend.app.endpoints;

import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.handler.Request;
import com.schibsted.backend.server.handler.Response;
import com.schibsted.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by ulises on 24/10/15.
 */
public class CORSEndpoint extends Endpoint<Void> {

    public CORSEndpoint(String pathExpression, HttpMethod httpMethod, ResponseBuilder responseBuilder, RequestParser<Void> requestParser) {
        super(pathExpression, responseBuilder, httpMethod, requestParser);
    }

    @Override
    public Response doCall(Request request, Void parsedRequest) throws Exception {
        return new Response(HttpResponseStatus.OK, request, ":)");
    }

    @Override
    public boolean match(String requestUri, HttpMethod method) {
        return super.match(requestUri, method);
    }
}
