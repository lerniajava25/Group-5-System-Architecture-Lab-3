package org.lab2.systemarchitecturegroup5assignment3;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * The central configuration entry point for the JAX-RS REST API.
 * The @ApplicationPath("/") annotation establishes the root context path for all endpoints.
 * This triggers WildFly to automatically scan and expose resources like PetResource.
 */
@ApplicationPath("/")
public class RestApplication extends Application {
    // This class remains empty as JAX-RS uses automatic classpath scanning in Jakarta EE 11.
}

