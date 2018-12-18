package com.tvestergaard.start.logic;

import com.tvestergaard.start.data.repositories.UserRepository;
import com.tvestergaard.start.logic.authentication.AuthenticationContext;
import com.tvestergaard.start.logic.authentication.AuthenticationException;
import com.tvestergaard.start.logic.authentication.TokenAuthenticator;
import com.tvestergaard.start.logic.authentication.UserAuthenticator;
import com.tvestergaard.start.logic.authentication.jwt.JwtGenerationException;
import com.tvestergaard.start.logic.authentication.jwt.JwtSecret;
import com.tvestergaard.start.logic.authentication.jwt.JwtTokenTransformer;
import com.tvestergaard.start.logic.authentication.jwt.JwtUnpackingException;

import java.util.function.Supplier;

public class AuthenticationFacade
{

    /**
     * The jwt secret used to generate jwt tokens.
     */
    private final JwtSecret jwtSecret;

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link AuthenticationFacade}.
     *
     * @param jwtSecret             The jwt secret used to generate jwt tokens.
     * @param userRepositoryFactory The factory that produces user repositories used by this facade.
     */
    public AuthenticationFacade(JwtSecret jwtSecret, Supplier<UserRepository> userRepositoryFactory)
    {
        this.jwtSecret = jwtSecret;
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Generates a JWT authentication token for the provided authentication context.
     *
     * @param authenticationContext The authentication context to generate the JWT token from.
     * @return The resulting JWT token.
     * @throws JwtGenerationException When the jwt token cannot be generated.
     */
    public String generateAuthenticationToken(AuthenticationContext authenticationContext) throws JwtGenerationException
    {
        JwtTokenTransformer generator = new JwtTokenTransformer(jwtSecret);
        return generator.generate(authenticationContext);
    }

    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The authentication context.
     * @throws AuthenticationException When the credentials could not be used to authenticate the user.
     */
    public AuthenticationContext authenticate(String email, String password) throws AuthenticationException
    {
        UserAuthenticator authenticator = new UserAuthenticator(userRepositoryFactory);
        return authenticator.authenticate(email, password);
    }

    /**
     * Performs authentication based on a provided JWT token.
     *
     * @param token The token to perform authentication upon
     * @return The resulting authentication context.
     * @throws AuthenticationException When the provided token cannot be authenticated.
     * @throws JwtUnpackingException   When the provided token cannot be unpacked.
     */
    public AuthenticationContext authenticateToken(String token) throws AuthenticationException, JwtUnpackingException
    {
        return new TokenAuthenticator(jwtSecret, userRepositoryFactory).authenticate(token);
    }

    /**
     * Authenticates based on a provided HTTP Authorization Bearer header.
     *
     * @param header The provided HTTP Authorization Bearer header,
     * @return The resulting AuthenticationContext.
     * @throws AuthenticationException When the HTTP Authorization Bearer cannot be authenticated.
     * @throws JwtUnpackingException   When the provided bearer token could not be unpacked.
     */
    public AuthenticationContext authenticateBearerHeader(String header) throws AuthenticationException, JwtUnpackingException
    {
        if (header == null)
            throw new AuthenticationException("Requires Authorization: Type <token>");

        header = header.trim();
        if (header.startsWith("Bearer ")) {
            header = header.substring(7);
            return this.authenticateToken(header);
        }

        throw new AuthenticationException("Unsupported HTTP Authorization type.");
    }
}
