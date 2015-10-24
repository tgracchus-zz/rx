package com.schibsted.frontend;

import com.schibsted.frontend.authorization.AuthorizationFilter;
import com.schibsted.frontend.authorization.AuthorizationServices;
import com.schibsted.frontend.authorization.RolDecider;
import com.schibsted.frontend.authorization.SessionIdParser;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 *  * A {@link ContextHandlerCollection} handler may be used to direct a request to
 *  * a specific Context. The URI path prefix and optional virtual host is used to
 *  * select the context.
 *  
 */
public class FrontEndServer {


    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        HttpClient client = new HttpClient();
        client.start();


        //Dynamic Content
        ServletContextHandler servletContextHandler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/*");
        servletContextHandler.setResourceBase(System.getProperty("java.io.tmpdir"));
        servletContextHandler.setBaseResource(Resource.newResource("src/main/resources"));
        servletContextHandler.addServlet(DefaultServlet.class, "/*");
        servletContextHandler.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

        //Authorization Filter
        FilterHolder authorizationFilter = new FilterHolder(
                new AuthorizationFilter(
                        new AuthorizationServices(client, new RolDecider(), new SessionIdParser()
                        )
                )
        );

        servletContextHandler.addFilter(authorizationFilter, "/pages/*",
                EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));


        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.addHandler(servletContextHandler);


        server.setHandler(contexts);

        // Starting the Server
        server.start();
        System.out.println("Started!");
        server.join();
    }
}