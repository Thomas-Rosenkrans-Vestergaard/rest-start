package com.tvestergaard.start.data.repositories;

import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.transactions.TransactionalRepository;

/**
 * Represents a data source of users. Defines read and write operations on the data source.
 */
public interface UserRepository extends CrudRepository<Integer, User>, TransactionalRepository
{

    /**
     * Inserts a new user into the repository with the provided information.
     *
     * @param name         The name of the new user.
     * @param email        The email of the new user.
     * @param passwordHash The password hash of the new user.
     * @return The resulting new user.
     */
    User createUser(String name, String email, String passwordHash);

    /**
     * Returns the user with the provided email.
     *
     * @param email The email fo the user to find and return.
     * @return The user with the provided email, {@code null} when no such user exists.
     */
    User getByEmail(String email);
}
