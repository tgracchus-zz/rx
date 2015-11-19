package com.backend.app.config;

import com.backend.app.dto.LogoutContext;
import com.backend.app.endpoints.*;
import com.backend.app.model.Credentials;
import com.backend.app.model.Roles;
import com.backend.app.services.DefaultSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.app.dto.AuthorizationContext;
import com.backend.app.dto.LoginContext;
import com.backend.app.model.Principal;
import com.schibsted.backend.server.endpoint.Endpoint;
import com.schibsted.backend.server.endpoint.Endpoints;
import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.handler.ResponseBuilder;
import com.schibsted.backend.server.handler.RxNettyHandler;
import com.schibsted.backend.server.parser.JsonRequestParser;
import com.schibsted.backend.server.parser.VoidRequestParser;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Could Had Used Guice for dependency injection here, but due to time I prefer to handle it manually
 * Created by ulises on 24/10/15.
 */
public class RequestHandlerFactory {

    public static RxNettyHandler newRxNettyHandler() {

        //Utilities
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseBuilder responseBuilder = new ResponseBuilder(objectMapper);


        //Credentials
        Credentials user1 = new Credentials.CredentialsBuilder()
                .principal(new Principal("user1", "user1")).role(Roles.PAGE_1).build();

        Credentials user2 = new Credentials.CredentialsBuilder()
                .principal(new Principal("user2", "user2")).role(Roles.PAGE_2).build();

        Credentials user3 = new Credentials.CredentialsBuilder()
                .principal(new Principal("user3", "user3")).role(Roles.PAGE_3).build();

        Credentials userAll = new Credentials.CredentialsBuilder()
                .principal(new Principal("userall", "userall")).role(Roles.PAGE_1).role(Roles.PAGE_2).role(Roles.PAGE_3).build();

        ConcurrentMap<String, Credentials> credentials = new ConcurrentHashMap<>();
        credentials.put(user1.getUserId(), user1);
        credentials.put(user2.getUserId(), user2);
        credentials.put(user3.getUserId(), user3);
        credentials.put(userAll.getUserId(), userAll);

        //SessionService
        DefaultSessionService sessionService = new DefaultSessionService(60 * 1000 * 5, credentials);
        sessionService.startExpiration();

        //HealthCheck Endpoint
        RequestParser<Void> voidRequestParse = new VoidRequestParser();
        Endpoint<Void> healthCheckEndpoint =
                new HealtCheckEnpoint("/healthcheck", responseBuilder, HttpMethod.GET, voidRequestParse);

        //Login Endpoint
        RequestParser<LoginContext> loginRequestParser = new JsonRequestParser(objectMapper, LoginContext.class);
        Endpoint<LoginContext> loginEndpoint =
                new LoginEndpoint("/login", HttpMethod.POST, responseBuilder, loginRequestParser, sessionService);

        //Logout Endpoint
        RequestParser<LogoutContext> logoutRequestParser = new JsonRequestParser(objectMapper, LogoutContext.class);
        Endpoint<LogoutContext> logoutEndpoint =
                new LogoutEndpoint("/logout", HttpMethod.POST, responseBuilder, logoutRequestParser, sessionService);

        //CORSEndpoint
        Endpoint<Void> corsEndpoint = new CORSEndpoint("/.*", HttpMethod.OPTIONS, responseBuilder, new VoidRequestParser());

        //Authorization Endpoint
        RequestParser<AuthorizationContext> authoRequestParser = new JsonRequestParser(objectMapper, AuthorizationContext.class);
        Endpoint<AuthorizationContext> authoEndpoint =
                new AuthorizationEndpoint("/authorization", HttpMethod.POST, responseBuilder, authoRequestParser, sessionService);


        Endpoints endpoints = new Endpoints(Arrays.asList(corsEndpoint, authoEndpoint, loginEndpoint, logoutEndpoint, healthCheckEndpoint));

        return new RxNettyHandler(endpoints);
    }
}
