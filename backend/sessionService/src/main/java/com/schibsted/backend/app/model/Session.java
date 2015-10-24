package com.schibsted.backend.app.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Session implements Delayed {

    private final String userId;
    private final UUID sessionId;
    private final long fixedDelay;

    private final Set<Role> roles;

    private volatile long delayInMilliseconds;

    public Session(UUID sessionId, String userId, long fixedDelay,Set<Role> roles) {
        super();
        this.sessionId = sessionId;
        this.fixedDelay = fixedDelay;
        this.delayInMilliseconds = System.currentTimeMillis() + this.fixedDelay;
        this.userId = userId;
        this.roles = new HashSet<>(roles);
    }

    public void refreshDelay() {
        this.delayInMilliseconds = System.currentTimeMillis() + this.fixedDelay;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = delayInMilliseconds - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (delayInMilliseconds - o.getDelay(TimeUnit.MINUTES));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Session session = (Session) o;

        if (userId != null ? !userId.equals(session.userId) : session.userId != null)
            return false;
        return !(sessionId != null ? !sessionId.equals(session.sessionId) : session.sessionId != null);

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        return result;
    }
}
