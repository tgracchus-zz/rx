package com.backend.app.endpoints;

import com.backend.app.services.SessionService;
import com.backend.app.dto.LoginContext;
import com.backend.app.model.Session;
import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.handler.Request;
import com.schibsted.backend.server.handler.Response;
import com.schibsted.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Optional;

public class LoginEndpoint extends Endpoint<LoginContext> {

    private final SessionService sessionService;

    public LoginEndpoint(String loginPath, HttpMethod httpMethod, ResponseBuilder responseBuilder, RequestParser<LoginContext> requestParser,
                         SessionService sessionService) {
        super(loginPath, responseBuilder, httpMethod, requestParser);
        this.sessionService = sessionService;
    }

    @Override
    public Response doCall(Request request, LoginContext loginContext) throws Exception {

        Optional<Session> session = sessionService.login(loginContext);

        if (session.isPresent()) {
            return newResponse(HttpResponseStatus.OK, request, session.get());
        } else {
            return newResponse(HttpResponseStatus.UNAUTHORIZED, request, String.valueOf(HttpResponseStatus.UNAUTHORIZED));
        }
    }

}

