package com.tvestergaard.start.logic.authentication.jwt;

import com.tvestergaard.start.logic.LogicException;

/**
 * Thrown when a JWT token could not be generated.
 */
public class JwtGenerationException extends LogicException
{

    /**
     * Creates a new {@code JwtGenerationException}.
     *
     * @param cause The cause of the failure to generate the token.
     */
    public JwtGenerationException(Throwable cause)
    {
        this("Could not generate a jwt token.", cause);
    }

    /**
     * Creates a new {@code JwtGenerationException}.
     *
     * @param errorMessage The error message provided to the exception.
     * @param cause        The cause of the failure to generate the token.
     */
    public JwtGenerationException(String errorMessage, Throwable cause)
    {
        super("JwtGenerationError", errorMessage, cause);
    }
}
