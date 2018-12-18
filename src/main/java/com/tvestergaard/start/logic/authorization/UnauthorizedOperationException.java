package com.tvestergaard.start.logic.authorization;

import com.tvestergaard.start.logic.LogicException;

/**
 * Thrown when a some actor attempts to perform an operation, that cannot be performed due
 * to some specific state.
 * <p>
 * When the operation cannot be perform because the actor lacks permission, an
 * {@link AuthorizationException} should be thrown instead.
 */
public class UnauthorizedOperationException extends LogicException
{

    public UnauthorizedOperationException(String errorMessage, Throwable cause)
    {
        this("UnauthorizedOperationError", errorMessage, cause);
    }

    public UnauthorizedOperationException(String errorName, String errorMessage, Throwable cause)
    {
        super(errorName, errorMessage, cause);
    }
}
