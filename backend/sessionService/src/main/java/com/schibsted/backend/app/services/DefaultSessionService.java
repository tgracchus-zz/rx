package com.schibsted.backend.app.services;

import com.schibsted.backend.app.dto.AuthorizationContext;
import com.schibsted.backend.app.dto.LoginContext;
import com.schibsted.backend.app.model.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;

/**
 * Created by ulises on 10/10/15.
 */
public class DefaultSessionService implements SessionService {

    private final ConcurrentMap<String, Credentials> credentialsByUser;

    private final ConcurrentMap<UUID, Session> sessionsById;
    private final ConcurrentMap<String, UUID> sessionByUser;
    private final DelayQueue<Session> expirationQueue;


    private long expirationInMilliseconds;

    public DefaultSessionService(long expirationInMilliseconds,
                                 ConcurrentMap<String, Credentials> credentialsByUser) {

        this.credentialsByUser = new ConcurrentHashMap<>(credentialsByUser);

        this.expirationQueue = new DelayQueue<>();
        this.sessionByUser = new ConcurrentHashMap<>();
        this.sessionsById = new ConcurrentHashMap<>();

        this.expirationInMilliseconds = expirationInMilliseconds;

    }

    @Override
    public Optional<Session> login(LoginContext loginContext) {

        Principal principal = new Principal(loginContext.getUserId(), loginContext.getPassword());
        return Optional.ofNullable(credentialsByUser.get(principal.getUserId()))
                .filter(credentials -> principal.equals(credentials.getPrincipal()))
                .map(credentials -> {
                    UUID sessionId = sessionByUser.computeIfAbsent(credentials.getUserId(), userId -> UUID.randomUUID());
                    Session session = sessionsById.computeIfAbsent(sessionId, uuid -> {
                        Session newSession = new Session(uuid, principal.getUserId(), expirationInMilliseconds,credentials.getRoles());
                        return newSession;
                    });

                    expirationQueue.add(session);
                    return session;
                });
    }


    @Override
    public Optional<Session> logout(UUID sessionId) {
        return
                Optional.ofNullable(sessionsById.computeIfPresent(sessionId, (currentSessionId, currentSession) -> {
                    Session deletedSession = sessionsById.remove(currentSessionId);
                    sessionByUser.remove(currentSession.getUserId());
                    return deletedSession;
                }));
    }

    @Override
    public boolean authorize(AuthorizationContext authorizationContext) {

        return Optional.ofNullable(sessionsById.get(authorizationContext.getSessionId()))
                .map(session -> {
                            boolean authorized = credentialsByUser.get(session.getUserId())
                                    .containsRole(new Role(authorizationContext.getRoleToCheck()));

                            session.refreshDelay();

                            return authorized;
                        }
                ).orElse(false);

    }

    @Override
    public SessionHealthCheck healtcheck() {
        return new SessionHealthCheck(sessionsById.size(), sessionByUser.size(), expirationQueue.size());
    }
}
