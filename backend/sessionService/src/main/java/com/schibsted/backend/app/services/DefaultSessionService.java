package com.schibsted.backend.app.services;

import com.schibsted.backend.app.dto.AuthorizationContext;
import com.schibsted.backend.app.dto.LoginContext;
import com.schibsted.backend.app.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by ulises on 10/10/15.
 */
public class DefaultSessionService implements SessionService {

    private final ConcurrentMap<String, Credentials> credentialsByUser;

    private final ConcurrentMap<UUID, Session> sessionsById;
    private final ConcurrentMap<String, UUID> sessionByUser;
    private final DelayQueue<Session> expirationQueue;

    private final ExecutorService executorService;

    private final long expirationInMilliseconds;

    public DefaultSessionService(long expirationInMilliseconds,
                                 ConcurrentMap<String, Credentials> credentialsByUser) {

        this.credentialsByUser = new ConcurrentHashMap<>(credentialsByUser);

        this.expirationQueue = new DelayQueue<>();
        this.sessionByUser = new ConcurrentHashMap<>();
        this.sessionsById = new ConcurrentHashMap<>();

        this.expirationInMilliseconds = expirationInMilliseconds;

        executorService =  Executors.newSingleThreadExecutor();

    }

    public void startExpiration(){
        executorService.execute(new ExpireThread(this,expirationInMilliseconds));
    }

    public void stopExpiration(){
        executorService.shutdown();
    }

    @Override
    public Optional<Session> login(LoginContext loginContext) {

        Principal principal = new Principal(loginContext.getUserId(), loginContext.getPassword());
        return Optional.ofNullable(credentialsByUser.get(principal.getUserId()))
                .filter(credentials -> principal.equals(credentials.getPrincipal()))
                .map(credentials -> {
                    UUID sessionId = sessionByUser.computeIfAbsent(credentials.getUserId(), userId -> UUID.randomUUID());
                    Session session = sessionsById.computeIfAbsent(sessionId, uuid -> {
                        Session newSession = new Session(uuid, principal.getUserId(), expirationInMilliseconds, credentials.getRoles());
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




    private static class ExpireThread implements Runnable {

        private final DefaultSessionService sessionService;

        private final long wakeUpTime;

        public ExpireThread(DefaultSessionService sessionService, long wakeUpTime) {
            this.sessionService = sessionService;
            this.wakeUpTime = wakeUpTime * 20000;
        }

        @Override
        public void run() {
            while (1 == 1) {
                expireTokens();
                LockSupport.parkNanos(wakeUpTime);

            }
        }

        private void expireTokens() {
            List<Session> sessions = new ArrayList<>();
            sessionService.expirationQueue.drainTo(sessions);
            sessions.forEach(session -> sessionService.logout(session.getSessionId()));

        }
    }

}
