package com.schibsted.backend.app.model;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class Role {

    private final String role;

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role1 = (Role) o;

        return !(role != null ? !role.equals(role1.role) : role1.role != null);

    }

    @Override public int hashCode() {
        return role != null ? role.hashCode() : 0;
    }
}
