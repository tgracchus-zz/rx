package com.backend.app.endpoints;

import com.backend.app.dto.LogoutContext;
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

public class LogoutEndpoint extends Endpoint<LogoutContext> {

    private final SessionService sessionService;

    public LogoutEndpoint(String logOutPath, HttpMethod httpMethod, ResponseBuilder responseBuilder,
                          RequestParser<LogoutContext> requestParser,
                          SessionService sessionService) {
        super(logOutPath, responseBuilder, httpMethod, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Observable<Response> doCall(Request request, LogoutContext logoutContext) throws Exception {
        return sessionService.logout(logoutContext.getSessionId())
                .flatMap(session -> {
                    if (session.isPresent()) {
                        return newResponse(HttpResponseStatus.OK, request, session.get());
                    } else {
                        return newResponse(HttpResponseStatus.UNAUTHORIZED, request, String.valueOf(HttpResponseStatus.UNAUTHORIZED));
                    }
                });

    }

}

