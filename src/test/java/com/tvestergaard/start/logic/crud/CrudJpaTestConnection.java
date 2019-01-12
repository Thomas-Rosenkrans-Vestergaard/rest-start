package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.JpaTestConnection;

import javax.persistence.EntityManager;

public class CrudJpaTestConnection extends JpaTestConnection
{

    private static CrudJpaTestConnection singleton;

    public static CrudJpaTestConnection getInstance()
    {
        if (singleton == null)
            singleton = new CrudJpaTestConnection();

        return singleton;
    }


    private CrudJpaTestConnection()
    {
        super(entityManagerFactory -> {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM CrudTestResource r").executeUpdate();
            entityManager.getTransaction().commit();
        });
    }
}
