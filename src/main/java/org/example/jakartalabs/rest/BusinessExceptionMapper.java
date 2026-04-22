package org.example.jakartalabs.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.jakartalabs.ejb.BusinessException;

import java.util.Map;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("status", 400, "error", ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
