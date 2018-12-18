package com.tvestergaard.start.logic.authentication;

public class IncorrectCredentialsException extends AuthenticationException
{

    /**
     * Creates a new {@link IncorrectCredentialsException} without a cause.
     */
    public IncorrectCredentialsException()
    {
        this(null);
    }

    /**
     * Creates a new {@link IncorrectCredentialsException} with a cause.
     *
     * @param cause The cause of the error.
     */
    public IncorrectCredentialsException(Throwable cause)
    {
        super("IncorrectCredentialsError", "The provided credentials are incorrect.", cause);
    }
}
