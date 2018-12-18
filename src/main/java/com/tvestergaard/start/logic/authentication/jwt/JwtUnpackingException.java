package com.tvestergaard.start.logic.authentication.jwt;

/**
 * Thrown when a provided JWT cannot be unpacked.
 */
public class JwtUnpackingException extends Exception
{

    /**
     * Thrown when a provided JWT cannot be unpacked.
     *
     * @param message The error message.
     */
    public JwtUnpackingException(String message)
    {
        super(message);
    }

    /**
     * Thrown when a provided JWT cannot be unpacked.
     *
     * @param message The error message.
     * @param cause   The cause of the error.
     */
    public JwtUnpackingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
