package com.backend.app.model;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class Principal {

    private final String userId;
    private final String password;

    public Principal(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Principal that = (Principal) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;
        return !(password != null ? !password.equals(that.password) : that.password != null);

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
