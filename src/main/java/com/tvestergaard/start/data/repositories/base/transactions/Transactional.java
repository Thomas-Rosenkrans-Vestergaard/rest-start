package com.tvestergaard.start.data.repositories.base.transactions;

public interface Transactional extends AutoCloseable
{

    /**
     * Begins the transaction.
     */
    void begin();

    /**
     * Commits the current transaction.
     */
    void commit();

    /**
     * Rolls back the current transaction.
     */
    void rollback();

    /**
     * Closes the transaction.
     */
    void close();
}
