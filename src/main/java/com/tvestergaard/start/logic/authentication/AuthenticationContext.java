package com.tvestergaard.start.logic.authentication;

import com.tvestergaard.start.data.entities.User;

public interface AuthenticationContext
{

    /**
     * Returns the type of the authenticated entity.
     *
     * @return The type of the authenticated entity.
     */
    AuthenticationType getType();

    /**
     * Returns the id of the authenticated user, {@code null} when the authentication context does not
     * contain that information.
     *
     * @return The id of the authenticated user, {@code null} to indicate that this authentication context does not
     * contain user information.
     */
    Integer getUserId();

    /**
     * Returns the authenticated user, or the represented user entity, {@code null} when the authentication context
     * does not contain that information.
     *
     * @return The authenticated user, {@code null} to indicate that this authentication context does not
     * contain user information.
     */
    User getUser();
}
