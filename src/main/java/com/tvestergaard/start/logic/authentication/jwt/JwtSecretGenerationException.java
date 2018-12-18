package com.tvestergaard.start.logic.authentication.jwt;

/**
 * Thrown when a secret for the generation and verification of a JWT, cannot be generated.
 */
public class JwtSecretGenerationException extends Exception
{

    public JwtSecretGenerationException(Throwable cause)
    {
        super("Could not generate secret for JWT.", cause);
    }
}
