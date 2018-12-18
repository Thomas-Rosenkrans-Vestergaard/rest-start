package com.tvestergaard.start.logic.authentication;

import com.tvestergaard.start.logic.LogicException;

public class AuthenticationException extends LogicException
{

    public AuthenticationException()
    {
        this((Throwable) null);
    }

    public AuthenticationException(String errorMessage)
    {
        this("AuthenticationError", errorMessage, null);
    }

    public AuthenticationException(Throwable cause)
    {
        this("Could not perform authentication.", cause);
    }

    public AuthenticationException(String errorName, String errorMessage, Throwable cause)
    {
        super(errorName, errorMessage, cause);
    }

    public AuthenticationException(String errorMessage, Throwable cause)
    {
        this("AuthenticationError", errorMessage, cause);
    }
}
