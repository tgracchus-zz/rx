package com.schibsted.backend.server.parser;

import com.schibsted.backend.server.endpoint.RequestParser;
import com.schibsted.backend.server.endpoint.RequestParserException;
import com.schibsted.backend.server.handler.Request;

/**
 * Created by ulises on 24/10/15.
 */
public class VoidRequestParser implements RequestParser<Void> {

    @Override
    public Void parse(Request request) throws RequestParserException {
        return null;
    }
}
