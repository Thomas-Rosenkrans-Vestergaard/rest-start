package com.tvestergaard.start.data.repositories.base.queries;

public class RepositoryQueryCompileException extends RepositoryQueryException
{

    public RepositoryQueryCompileException(String message)
    {
        super(message);
    }

    public RepositoryQueryCompileException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
