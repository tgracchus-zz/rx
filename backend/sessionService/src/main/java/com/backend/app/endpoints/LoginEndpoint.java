package com.backend.app.endpoints;

import com.backend.app.dto.LoginContext;
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

public class LoginEndpoint extends Endpoint<LoginContext> {

    private final SessionService sessionService;

    public LoginEndpoint(String loginPath, HttpMethod httpMethod, ResponseBuilder responseBuilder, RequestParser<LoginContext> requestParser,
                         SessionService sessionService) {
        super(loginPath, responseBuilder, httpMethod, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Observable<Response> doCall(Request request, LoginContext loginContext) throws Exception {
        return sessionService.login(loginContext)
                .flatMap(session -> {
                    if (session.isPresent()) {
                        return newResponse(HttpResponseStatus.OK, request, session.get());
                    } else {
                        return newResponse(HttpResponseStatus.UNAUTHORIZED, request, String.valueOf(HttpResponseStatus.UNAUTHORIZED));
                    }
                });
    }

}

