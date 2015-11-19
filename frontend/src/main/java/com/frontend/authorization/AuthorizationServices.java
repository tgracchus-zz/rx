package com.frontend.authorization;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.FutureResponseListener;
import org.eclipse.jetty.client.util.StringContentProvider;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by ulises on 24/10/15.
 */
public class AuthorizationServices {

    private final HttpClient client;
    private final RolDecider rolDecider;

    public AuthorizationServices(HttpClient client, RolDecider rolDecider) {
        this.client = client;
        this.rolDecider = rolDecider;
    }


    public boolean authorize(String requestUri, String sessionId) {

        Optional<String> rolOpt = rolDecider.parseRol(requestUri);

        if (rolOpt.isPresent()){
            StringContentProvider sessionContext =
                    new StringContentProvider("{\"sessionId\":\"" + sessionId + "\",\"roleToCheck\":\"" + rolOpt.get() + "\"}");

            Request request = client.POST("http://localhost:8888/authorization")
                    .content(sessionContext);
            FutureResponseListener listener = new FutureResponseListener(request, 512 * 1024);
            try {
                request.send(listener);
                return Boolean.valueOf(listener.get().getContentAsString());

            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }

        return true;
    }

}
