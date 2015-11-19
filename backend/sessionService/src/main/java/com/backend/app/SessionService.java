package com.backend.app;


import com.backend.app.config.RequestHandlerFactory;
import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrapModule;
import netflix.karyon.servo.KaryonServoModule;



/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class SessionService {

    public static void main(String[] args) {

        HealthCheck healthCheckHandler = new HealthCheck();

        Karyon.forRequestHandler(8888, RequestHandlerFactory.newRxNettyHandler(),
                new KaryonBootstrapModule(healthCheckHandler), new ArchaiusBootstrapModule("session"),
                Karyon.toBootstrapModule(KaryonWebAdminModule.class), ShutdownModule.asBootstrapModule(),
                KaryonServoModule.asBootstrapModule()).startAndWaitTillShutdown();

    }


}
