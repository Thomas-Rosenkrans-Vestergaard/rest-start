package com.tvestergaard.start.logic.authentication;

import com.tvestergaard.start.data.entities.User;

import static com.tvestergaard.start.logic.authentication.AuthenticationType.USER;

public class EagerAuthenticationContext implements AuthenticationContext
{

    /**
     * The type of the authenticated entity.
     */
    private AuthenticationType type;

    /**
     * The authenticated user.
     */
    private User user;

    /**
     * Creates a new {@link AuthenticationContext}.
     *
     * @param type The type of the authenticated entity.
     * @param user The authenticated user, non-null when the authentication context contains an authenticated user.
     */
    private EagerAuthenticationContext(AuthenticationType type, User user)
    {
        assert type != null;
        if (type == USER)
            assert user != null;

        this.type = type;
        this.user = user;
    }

    /**
     * Creates a new authentication context of the type {@code USER} using the provided user.
     *
     * @param user The authenticated user.
     * @return The newly created authentication context.
     */
    public static EagerAuthenticationContext user(User user)
    {
        return new EagerAuthenticationContext(USER, user);
    }


    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    public AuthenticationType getType()
    {
        return this.type;
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
        if (this.user == null)
            return null;

        return user.getId();
    }

    /**
     * Returns the authenticated user, or the represented user entity, {@code null} when the authentication context
     * does not contain that information.
     *
     * @return The authenticated user, {@code null} to indicate that this authentication context does not
     * contain user information.
     */
    @Override public User getUser()
    {
        return this.user;
    }
}