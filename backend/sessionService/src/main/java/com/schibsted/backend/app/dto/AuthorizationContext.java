package com.schibsted.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.schibsted.backend.app.model.Role;

import java.util.UUID;

/**
 * Created by ulises on 24/10/15.
 */
public class AuthorizationContext {

    private final UUID sessionId;
    private final String roleToCheck;

    @JsonCreator
    public AuthorizationContext(@JsonProperty("sessionId") UUID sessionId, @JsonProperty("roleToCheck") String roleToCheck) {
        Preconditions.checkNotNull(sessionId);
        Preconditions.checkNotNull(roleToCheck);
        this.sessionId = sessionId;
        this.roleToCheck = roleToCheck;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getRoleToCheck() {
        return roleToCheck;
    }
}
