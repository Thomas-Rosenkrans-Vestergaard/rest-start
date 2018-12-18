package com.tvestergaard.start.logic.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.UserRepository;
import com.tvestergaard.start.logic.authentication.jwt.AuthenticationToken;
import com.tvestergaard.start.logic.authentication.jwt.JwtSecret;
import com.tvestergaard.start.logic.authentication.jwt.JwtTokenTransformer;
import com.tvestergaard.start.logic.authentication.jwt.JwtUnpackingException;

import java.util.function.Supplier;

public class TokenAuthenticator
{

    /**
     * The jwt secret used when authenticating users.
     */
    private final JwtSecret jwtSecret;

    /**
     * The factory that creates user repositories used during authentication.
     */
    private final Supplier<UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link TokenAuthenticator}.
     *
     * @param jwtSecret             The jwt secret used when authenticating users.
     * @param userRepositoryFactory The factory that created user repositories used during authentication.
     */
    public TokenAuthenticator(JwtSecret jwtSecret, Supplier<UserRepository> userRepositoryFactory)
    {
        this.jwtSecret = jwtSecret;
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Authenticated the incoming token, returning an authentication context with information about the authenticated
     * user.
     *
     * @param token The token to authenticate.
     * @return The authentication context containing information about the authenticated user.
     * @throws AuthenticationException When the token cannot be authenticated.
     */
    public AuthenticationContext authenticate(String token) throws AuthenticationException, JwtUnpackingException
    {
        try {

            JwtTokenTransformer tokenTransformer = new JwtTokenTransformer(jwtSecret);
            AuthenticationToken unpacked         = tokenTransformer.unpack(token);
            AuthenticationType  type             = AuthenticationType.valueOf(unpacked.getType());

            if (type == AuthenticationType.USER) {
                Integer user = unpacked.getUser();
                return LazyAuthenticationContext.user(user, createUserRetriever(user));
            }

            throw new AuthenticationException("Unsupported authentication type.");

        } catch (JWTVerificationException e) {
            throw new AuthenticationException("Could not verify authentication token.", e);
        }
    }

    /**
     * Creates a supplier that retrieves a user based on the provided id.
     *
     * @param id The id of the user to retrieve.
     * @return The supplier that retrieves the user with the provided id.
     */
    private Supplier<User> createUserRetriever(Integer id)
    {
        return () -> {
            try (UserRepository userRepository = userRepositoryFactory.get()) {
                return userRepository.get(id);
            }
        };
    }
}
