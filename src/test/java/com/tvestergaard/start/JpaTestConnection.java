package com.tvestergaard.start;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class JpaTestConnection
{
    private static EntityManagerFactory _emf;

    public static EntityManagerFactory create()
    {
        if (_emf == null)
            _emf = Persistence.createEntityManagerFactory("rest-api-test-pu");

        return _emf;
    }

    protected final EntityManagerFactory emf;

    public JpaTestConnection()
    {
        this.emf = Persistence.createEntityManagerFactory("rest-api-test-pu");
    }

    public abstract void reset();
}
