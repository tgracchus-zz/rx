package com.backend.server.endpoint;

/**
 * Created by ulises on 10/10/15.
 */
public class RequestParserException extends Exception {

    public RequestParserException(String message) {
        super(message);
    }

    public RequestParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParserException(Throwable cause) {
        super(cause);
    }

    public RequestParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RequestParserException() {
    }
}
