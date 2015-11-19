package com.frontend.login;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.FutureResponseListener;
import org.eclipse.jetty.client.util.StringContentProvider;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by ulises on 10/11/15.
 */
public class LoginServices {

    private final HttpClient client;

    public LoginServices(HttpClient client) {
        this.client = client;
    }


    public Optional<String> login(String userId, String password) {

        StringContentProvider sessionContext =
                new StringContentProvider("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}");

        Request request = client.POST("http://localhost:8888/login")
                .content(sessionContext);

        FutureResponseListener listener = new FutureResponseListener(request, 512 * 1024);
        try {
            request.send(listener);
            return Optional.of(listener.get().getContentAsString());

        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }

    }

    public boolean logout(String sessionId) {

        StringContentProvider sessionContext =
                new StringContentProvider("{\"sessionId\":\"" + sessionId + "\"");

        Request request = client.POST("http://localhost:8888/logout")
                .content(sessionContext);

        FutureResponseListener listener = new FutureResponseListener(request, 512 * 1024);
        try {
            request.send(listener);
            listener.get().getContentAsString();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
}
