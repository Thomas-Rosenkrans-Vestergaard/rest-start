package com.tvestergaard.start.logic.authentication.jwt;

public interface AuthenticationToken
{

    /**
     * Returns the type of the authentication used.
     *
     * @return The type of the authentication used.
     */
    String getType();

    /**
     * Returns the id of the user.
     *
     * @return The id of the user.
     */
    Integer getUser();
}
