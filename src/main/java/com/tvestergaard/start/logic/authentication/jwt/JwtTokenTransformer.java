package com.tvestergaard.start.logic.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.logic.authentication.AuthenticationContext;

import java.util.Date;

/**
 * Generates JWT authentication tokens for users.
 */
public class JwtTokenTransformer
{

    private static final String ISS      = "com.tvestergaard";
    private static final String TYPE_KEY = "type";
    private static final String USER_KEY = "user";

    /**
     * The secret to use when generating JWT authentication tokens.
     */
    private final JwtSecret secret;

    /**
     * Creates a new {@link JwtTokenTransformer}.
     *
     * @param secret The secret to use when generating JWT authentication tokens.
     */
    public JwtTokenTransformer(JwtSecret secret)
    {
        this.secret = secret;
    }

    /**
     * Generates a new JWT token for the provided authentication context, signed using the configured secret.
     *
     * @param authenticationContext The authentication context to tokenize.
     * @return The JWT token.
     * @throws JwtGenerationException When the method could not generate a Jwt token.
     */
    public String generate(AuthenticationContext authenticationContext) throws JwtGenerationException
    {
        try {
            Algorithm          algorithm = Algorithm.HMAC256(secret.getValue());
            JWTCreator.Builder builder   = JWT.create().withIssuer(ISS).withIssuedAt(new Date());

            builder.withClaim(TYPE_KEY, authenticationContext.getType().name());

            User user = authenticationContext.getUser();
            if (user != null)
                builder.withClaim(USER_KEY, user.getId());

            return builder.sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JwtGenerationException(exception);
        }
    }

    /**
     * Verifies and unpacks the payload of the provided JWT token using the configured secret.
     *
     * @param token The token to authenticate and unpack.
     * @return The contents of the token.
     * @throws JwtUnpackingException When the provided token could not be unpacked.
     */
    public AuthenticationToken unpack(String token) throws JwtUnpackingException
    {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getValue());
            DecodedJWT decodedJWT = JWT.require(algorithm)
                                       .withIssuer(ISS)
                                       .build()
                                       .verify(token);

            return new AuthenticationToken()
            {
                @Override public String getType()
                {
                    return decodedJWT.getClaim(TYPE_KEY).asString();
                }

                @Override public Integer getUser()
                {
                    return decodedJWT.getClaim(USER_KEY).asInt();
                }
            };

        } catch (JWTVerificationException e) {
            throw new JwtUnpackingException("Could not verify JWT.", e);
        }
    }
}
