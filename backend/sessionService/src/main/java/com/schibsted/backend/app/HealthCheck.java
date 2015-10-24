
package com.schibsted.backend.app;

import com.google.inject.Singleton;
import netflix.karyon.health.HealthCheckHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Singleton public class HealthCheck implements HealthCheckHandler {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheck.class);

    @PostConstruct public void init() {
        logger.info("Health check initialized.");
    }

    @Override public int getStatus() {
        logger.info("Health check invoked.");
        return 200;
    }
}
