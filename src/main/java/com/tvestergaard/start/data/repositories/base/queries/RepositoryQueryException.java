package com.tvestergaard.start.data.repositories.base.queries;

public class RepositoryQueryException extends RuntimeException
{

    public RepositoryQueryException(String message)
    {
        super(message);
    }

    public RepositoryQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
