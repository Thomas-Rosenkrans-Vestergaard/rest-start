package com.tvestergaard.start;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaTestConnection
{
    private static EntityManagerFactory emf;

    public static EntityManagerFactory create()
    {
        if (emf == null)
            emf = Persistence.createEntityManagerFactory("rest-api-test-pu");

        return emf;
    }
}
