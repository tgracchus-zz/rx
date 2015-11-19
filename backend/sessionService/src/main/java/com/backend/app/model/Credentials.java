package com.backend.app.model;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class Credentials {

    private final Principal principal;
    private final Set<Role> roles;

    private Credentials(Principal principal, Set<Role> roles) {
        this.principal = principal;
        this.roles = new CopyOnWriteArraySet<>(roles);
    }

    public Principal getPrincipal() {
        return principal;
    }

    public boolean containsRole(Role role) {
        return roles.contains(role);
    }

    public String getUserId() {
        return principal.getUserId();
    }


    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(new HashSet<>(roles));
    }

    public static class CredentialsBuilder {

        private Principal principal;
        private final Set<Role> roles;

        public CredentialsBuilder() {
            this.roles = new HashSet<>();
        }

        public CredentialsBuilder principal(Principal principal) {
            this.principal = principal;
            return this;
        }

        public CredentialsBuilder role(Role role) {
            roles.add(role);
            return this;
        }

        public Credentials build() {
            Preconditions.checkNotNull(principal);
            Preconditions.checkState(!roles.isEmpty());
            return new Credentials(principal, roles);
        }


    }
}
