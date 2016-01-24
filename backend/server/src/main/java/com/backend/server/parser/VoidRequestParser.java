package com.backend.server.parser;

import com.backend.server.endpoint.RequestParser;
import com.backend.server.endpoint.RequestParserException;
import com.backend.server.handler.Request;

/**
 * Created by ulises on 24/10/15.
 */
public class VoidRequestParser implements RequestParser<Void> {

    @Override
    public Void parse(Request request) throws RequestParserException {
        return null;
    }
}
