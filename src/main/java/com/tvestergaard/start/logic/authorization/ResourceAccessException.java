package com.tvestergaard.start.logic.authorization;

public class ResourceAccessException extends UnauthorizedOperationException
{

    public ResourceAccessException(Class resource, Object id)
    {
        super("ResourceAccessException",
              String.format("You do not have access to view the %s with key %s",
                            resource.getSimpleName(),
                            String.valueOf(id)),
              null
        );
    }
}
