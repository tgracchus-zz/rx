package com.schibsted.backend.app.endpoints;


import com.schibsted.backend.app.dto.AuthorizationContext;
import com.schibsted.backend.app.services.SessionService;
import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.handler.Request;
import com.schibsted.backend.server.handler.Response;
import com.schibsted.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

public class AuthorizationEndpoint extends Endpoint<AuthorizationContext> {

    private final SessionService sessionService;

    public AuthorizationEndpoint(String path, HttpMethod method, ResponseBuilder responseBuilder,
                                 RequestParser<AuthorizationContext> requestParser,
                                 SessionService sessionService) {
        super(path, responseBuilder, method, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Response doCall(Request request, AuthorizationContext authorizationContext) throws Exception {
        boolean authorized = sessionService.authorize(authorizationContext);

        return newResponse(HttpResponseStatus.OK, request, new Boolean(authorized));

    }

}

