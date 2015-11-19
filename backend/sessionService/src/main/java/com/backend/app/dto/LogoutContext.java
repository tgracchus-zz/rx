package com.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by ulises on 24/10/15.
 */
public class LogoutContext {

    private final UUID sessionId;

    @JsonCreator
    public LogoutContext(@JsonProperty("sessionId") UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }
}
