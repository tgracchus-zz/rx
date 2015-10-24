package com.schibsted.backend.server.endpoint;

import com.schibsted.backend.server.handler.Request;

/**
 * Created by ulises on 10/10/15.
 */
public interface RequestParser<T> {

    T parse(Request request) throws RequestParserException;
}
