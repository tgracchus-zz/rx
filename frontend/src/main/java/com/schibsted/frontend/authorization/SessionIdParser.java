package com.schibsted.frontend.authorization;

import org.eclipse.jetty.util.StringUtil;

import java.util.Optional;

/**
 * Created by ulises on 24/10/15.
 */
public class SessionIdParser {

    public Optional<String> parseSessionId(String queryString) {

        if (queryString == null) {
            return Optional.empty();
        }
        int sessionPosition = queryString.lastIndexOf("session=") + "session=".length();
        String sessionId = queryString.substring(sessionPosition, queryString.length());

        if(StringUtil.isBlank(sessionId)){
            return Optional.empty();
        }

        return Optional.ofNullable(sessionId);
    }
}
