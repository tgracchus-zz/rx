package com.backend.app.services;

import com.backend.app.dto.AuthorizationContext;
import com.backend.app.dto.LoginContext;
import com.backend.app.model.Session;
import com.backend.app.model.SessionHealthCheck;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by ulises on 10/10/15.
 */
public interface SessionService {

    Optional<Session> login(LoginContext principal);

    Optional<Session> logout(UUID sessionId);

    boolean authorize(AuthorizationContext authorizationContext);

    SessionHealthCheck healtcheck();
}
