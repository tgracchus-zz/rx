package com.backend.server.endpoint;

import com.backend.server.handler.Request;
import com.backend.server.handler.Response;
import com.backend.server.handler.ResponseBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ulises on 8/10/15.
 */
public abstract class Endpoint<T> {

    private final ResponseBuilder responseBuilder;
    private final URIMatcher uriMatcher;
    private final HttpMethod httpMethod;
    private final RequestParser<T> requestParser;

    public Endpoint(String pathExpression, ResponseBuilder responseBuilder, HttpMethod httpMethod,
                    RequestParser<T> requestParser) {
        this.uriMatcher = new RegexURIMatcher(pathExpression);
        this.responseBuilder = responseBuilder;
        this.httpMethod = httpMethod;
        this.requestParser = requestParser;
    }

    private final Logger log = Logger.getLogger(Endpoint.class.getName());

    public abstract Observable<Response> doCall(Request request, T parsedRequest) throws Exception;

    public Observable<Response> call(Request request) {
        try {

            T requestObject = requestParser.parse(request);
            return doCall(request, requestObject);

        } catch (RequestParserException e) {
            log.log(Level.INFO, e.getLocalizedMessage(), e);
            return newResponse(HttpResponseStatus.BAD_REQUEST, request, e.getMessage());

        } catch (Exception e) {
            log.log(Level.INFO, e.getLocalizedMessage(), e);
            return newServerErrorResponse(request, e.getMessage());
        }

    }

    public boolean match(String requestUri, HttpMethod method) {
        if (method.equals(httpMethod)) {
            return uriMatcher.match(requestUri);
        }
        return false;
    }

    protected Observable<Response> newResponse(HttpResponseStatus status, Request request, Object response) {
        return responseBuilder.newResponse(status, request, response);
    }

    protected Observable<Response> newServerErrorResponse(Request request, Object response) {
        return responseBuilder.newResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, request, response);
    }
}
