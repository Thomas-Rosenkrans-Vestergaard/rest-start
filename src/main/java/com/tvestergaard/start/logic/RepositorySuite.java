package com.tvestergaard.start.logic;

import com.tvestergaard.start.data.repositories.UserRepository;
import com.tvestergaard.start.data.repositories.base.transactions.Transaction;

public interface RepositorySuite<T extends Transaction>
{

    /**
     * Returns a new user repository.
     *
     * @return The new user repository.
     */
    UserRepository getUserRepository(T transaction);
}
