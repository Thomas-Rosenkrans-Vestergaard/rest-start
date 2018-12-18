package com.tvestergaard.start.logic.authorization;

import com.tvestergaard.start.logic.LogicException;

public class AuthorizationException extends LogicException
{


    public AuthorizationException(String errorName, String errorMessage, Throwable cause)
    {
        super(errorName, errorMessage, cause);
    }

    public AuthorizationException(String errorMessage, Throwable cause)
    {
        this("AuthorizationError", errorMessage, cause);
    }
}
