package com.backend.app.endpoints;


import com.backend.app.dto.AuthorizationContext;
import com.backend.app.services.SessionService;
import com.backend.server.endpoint.Endpoint;
import com.backend.server.endpoint.RequestParser;
import com.backend.server.handler.Request;
import com.backend.server.handler.Response;
import com.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;
import rx.schedulers.Schedulers;

public class AuthorizationEndpoint extends Endpoint<AuthorizationContext> {

    private final SessionService sessionService;

    public AuthorizationEndpoint(String path, HttpMethod method, ResponseBuilder responseBuilder,
                                 RequestParser<AuthorizationContext> requestParser,
                                 SessionService sessionService) {
        super(path, responseBuilder, method, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Observable<Response> doCall(Request request, AuthorizationContext authorizationContext) throws Exception {
        return sessionService.authorize(authorizationContext)
                .flatMap(authorized -> (newResponse(HttpResponseStatus.OK, request, new Boolean(authorized))));

    }

}

