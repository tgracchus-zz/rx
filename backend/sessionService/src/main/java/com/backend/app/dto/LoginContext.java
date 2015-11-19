package com.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ulises on 24/10/15.
 */
public class LoginContext {

    private final String userId;
    private final String password;

    @JsonCreator
    public LoginContext(@JsonProperty("userId") String userId, @JsonProperty("password") String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
