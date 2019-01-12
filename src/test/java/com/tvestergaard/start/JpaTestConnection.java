package com.tvestergaard.start;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.function.Consumer;

public class JpaTestConnection
{

    private static int                            nextUnique = 1;
    private final  EntityManagerFactory           entityManagerFactory;
    private final  Consumer<EntityManagerFactory> resetter;

    private static EntityManagerFactory global;

    public static EntityManagerFactory create()
    {
        if (global == null)
            global = Persistence.createEntityManagerFactory("rest-api-test-pu");

        return global;
    }

    public JpaTestConnection(Consumer<EntityManagerFactory> resetter)
    {
        HashMap<String, String> options = new HashMap<>();
        options.put("hibernate.connection.url", "jdbc:derby:memory:unit-testing-" + nextUnique++ + ";create=true");
        entityManagerFactory = Persistence.createEntityManagerFactory("rest-api-test-pu", options);
        this.resetter = resetter;
    }

    public EntityManagerFactory getEntityManagerFactory()
    {
        return this.entityManagerFactory;
    }

    public void reset()
    {
        resetter.accept(this.entityManagerFactory);
    }

    public void close()
    {
        getEntityManagerFactory().close();
    }
}
