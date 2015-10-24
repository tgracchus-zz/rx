package com.schibsted.frontend.authorization;

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
    private final SessionIdParser sessionIdParser;

    public AuthorizationServices(HttpClient client, RolDecider rolDecider, SessionIdParser sessionIdParser) {
        this.client = client;
        this.rolDecider = rolDecider;
        this.sessionIdParser = sessionIdParser;
    }


    public boolean authorize(String requestUri, String queryString) {


        Optional<String> rolOpt = rolDecider.parseRol(requestUri);
        Optional<String> sessionIdOpt = sessionIdParser.parseSessionId(queryString);

        if (rolOpt.isPresent() && sessionIdOpt.isPresent()) {
            StringContentProvider sessionContext =
                    new StringContentProvider("{\"sessionId\":\"" + sessionIdOpt.get() + "\",\"roleToCheck\":\"" + rolOpt.get() + "\"}");

            Request request = client.POST("http://localhost:8888/authorization")
                    .content(sessionContext);
            FutureResponseListener listener = new FutureResponseListener(request, 512 * 1024);
            try {
                request.send(listener);
                return Boolean.valueOf(listener.get().getContentAsString());

            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        } else if (rolOpt.isPresent()) {
            return false;
        }
        return true;
    }

}
