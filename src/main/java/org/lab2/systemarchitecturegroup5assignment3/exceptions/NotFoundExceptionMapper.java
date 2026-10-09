package org.lab2.systemarchitecturegroup5assignment3.exceptions;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        // Flattened onto a single line to ensure compatibility with Windows CMD
        String json = "{\"error\":\"Not Found\",\"message\":\"" + exception.getMessage() + "\"}";

        return Response.status(Response.Status.NOT_FOUND)
                .entity(json)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
