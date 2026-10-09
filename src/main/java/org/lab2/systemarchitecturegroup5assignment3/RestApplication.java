package org.lab2.systemarchitecturegroup5assignment3;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.lab2.systemarchitecturegroup5assignment3.exceptions.NotFoundExceptionMapper;
import org.lab2.systemarchitecturegroup5assignment3.exceptions.ValidationExceptionMapper;
import org.lab2.systemarchitecturegroup5assignment3.resource.PetResource;

import java.util.HashSet;
import java.util.Set;

/**
 * The central configuration entry point for the JAX-RS REST API.
 * The @ApplicationPath("/") annotation establishes the root context path for all endpoints.
 * This triggers WildFly to automatically scan and expose resources like PetResource.
 */
@ApplicationPath("/api") // Setting this explicitly to match your working URL path
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Explicitly register your endpoint
        classes.add(PetResource.class);
        // Explicitly register your custom exception handlers to override WildFly defaults
        classes.add(ValidationExceptionMapper.class);
        classes.add(NotFoundExceptionMapper.class);
        return classes;
    }
}

