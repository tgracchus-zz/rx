package com.backend.app.model;

/**
 * Created by ulises on 24/10/15.
 */
public class SessionHealthCheck {

    private final int numberOfSessions;
    private final int numberOfUsers;
    private final int numberOfQueue;

    public SessionHealthCheck(int numberOfSessions, int numberOfUsers, int numberOfQueue) {
        this.numberOfSessions = numberOfSessions;
        this.numberOfQueue = numberOfQueue;
        this.numberOfUsers = numberOfUsers;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public int getNumberOfQueue() {
        return numberOfQueue;
    }
}
