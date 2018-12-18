package com.tvestergaard.start.logic.authentication;

import com.tvestergaard.start.data.entities.User;

import java.util.function.Supplier;

/**
 * Implementation of {@link AuthenticationContext} that lazily fetches entities.
 */
public class LazyAuthenticationContext implements AuthenticationContext
{

    /**
     * The authentication type of this context.
     */
    private final AuthenticationType authenticationType;

    /**
     * The user represented by this authentication context.
     */
    private final Integer userId;

    /**
     * The supplier that lazily fetches the first user instance. Note that the supplier
     * does not need to implement caching, this class returns that first result on subsequent
     * requests.
     */
    private Supplier<User> userSupplier;
    private boolean        userHasBeenFetched = false;
    private User           fetchedUser;

    /**
     * Creates a new {@link LazyAuthenticationContext}.
     *
     * @param authenticationType The type of the {@link LazyAuthenticationContext}.
     * @param userId             The id of the user represented in the constructed authentication context.
     * @param userSupplier       The supplier that lazily fetches the first user instance. Note that the supplier
     *                           does not need to implement caching, this class returns that first result on subsequent
     *                           requests.
     * @return The newly constructed authentication context.
     */
    public LazyAuthenticationContext(AuthenticationType authenticationType, Integer userId, Supplier<User> userSupplier)
    {
        assert authenticationType != null;
        assert userSupplier != null;

        this.authenticationType = authenticationType;
        this.userId = userId;
        this.userSupplier = userSupplier;
    }

    /**
     * Constructs and returns a new {@link LazyAuthenticationContext} of the type {@link AuthenticationType#USER} with
     * the provided {@code userId} and {@code userSupplier}.
     *
     * @param userId       The id of the user represented in the constructed authentication context.
     * @param userSupplier The supplier that lazily fetches the first user instance. Note that the supplier
     *                     does not need to implement caching, this class returns that first result on subsequent
     *                     requests.
     * @return The newly constructed authentication context.
     */
    public static LazyAuthenticationContext user(Integer userId, Supplier<User> userSupplier)
    {
        return new LazyAuthenticationContext(AuthenticationType.USER, userId, userSupplier);
    }

    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    @Override
    public AuthenticationType getType()
    {
        return authenticationType;
    }

    /**
     * Returns the id of the authenticated user, {@code null} when the authentication context does not
     * contain that information.
     *
     * @return The id of the authenticated user, {@code null} to indicate that this authentication context does not
     * contain user information.
     */
    @Override public Integer getUserId()
    {
        return this.userId;
    }

    /**
     * Returns the authenticated user, or the represented user entity, {@code null} when the authentication context
     * does not contain that information. The fetching of the user is done lazily. The same user is returned on each
     * subsequent call.
     *
     * @return The authenticated user, {@code null} to indicate that this authentication context does not
     * contain user information.
     */
    @Override public User getUser()
    {
        if (!this.userHasBeenFetched) {
            this.fetchedUser = userSupplier.get();
            this.userHasBeenFetched = true;
        }

        return this.fetchedUser;
    }
}
