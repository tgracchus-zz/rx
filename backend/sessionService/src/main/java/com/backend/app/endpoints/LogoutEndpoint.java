package com.backend.app.endpoints;

import com.backend.app.dto.LogoutContext;
import com.backend.app.services.SessionService;
import com.backend.app.model.Session;
import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.handler.Request;
import com.schibsted.backend.server.handler.Response;
import com.schibsted.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Optional;

public class LogoutEndpoint extends Endpoint<LogoutContext> {

    private final SessionService sessionService;

    public LogoutEndpoint(String logOutPath, HttpMethod httpMethod, ResponseBuilder responseBuilder,
                          RequestParser<LogoutContext> requestParser,
                          SessionService sessionService) {
        super(logOutPath, responseBuilder, httpMethod, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Response doCall(Request request, LogoutContext logoutContext) throws Exception {

        Optional<Session> session = sessionService.logout(logoutContext.getSessionId());

        if (session.isPresent()) {
            return newResponse(HttpResponseStatus.OK, request, session.get());
        } else {
            return newResponse(HttpResponseStatus.OK, request, "");
        }
    }

}

