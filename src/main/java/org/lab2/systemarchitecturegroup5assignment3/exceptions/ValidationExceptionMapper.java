package org.lab2.systemarchitecturegroup5assignment3.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        StringBuilder json = new StringBuilder();
        json.append("{\"error\":\"Bad Request\",\"Status\":\"400\",\"message\":\"Validation failed\",\"errors\":{");

        boolean first = true;
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            if (!first) {
                json.append(",");
            }
            String fieldName = violation.getPropertyPath().toString();
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }

            json.append("\"").append(fieldName).append("\":\"")
                    .append(violation.getMessage()).append("\"");
            first = false;
        }

        json.append("}}");

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(json.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
