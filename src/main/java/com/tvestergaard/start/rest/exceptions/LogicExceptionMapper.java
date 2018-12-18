package com.tvestergaard.start.rest.exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvestergaard.start.logic.LogicException;
import com.tvestergaard.start.logic.ResourceConflictException;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import com.tvestergaard.start.logic.authentication.AuthenticationException;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LogicExceptionMapper implements ExceptionMapper<LogicException>
{

    private static Gson                gson  = new GsonBuilder().setPrettyPrinting().create();
    private static Map<Class, Integer> codes = new HashMap<>();

    static {
        codes.put(ResourceNotFoundException.class, 404);
        codes.put(AuthenticationException.class, 401);
        codes.put(ResourceConflictException.class, 409);
    }

    @Context
    private ServletContext context;

    @Override
    public Response toResponse(LogicException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.errorName = exception.getErrorName();
        exceptionResponse.message = exception.getErrorMessage();
        exceptionResponse.responseCode = codes.getOrDefault(exception.getClass(), 500);
        exceptionResponse.debug = isDebug;
        if (isDebug)
            exceptionResponse.cause = exception.getCause();

        return Response.status(exceptionResponse.responseCode).entity(gson.toJson(exceptionResponse)).build();
    }
}
