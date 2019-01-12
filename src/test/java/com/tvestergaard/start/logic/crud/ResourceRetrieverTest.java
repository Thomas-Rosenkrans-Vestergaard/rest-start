package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepository;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceRetrieverTest
{
    private static CrudJpaTestConnection                               connection = CrudJpaTestConnection.getInstance();
    private static Supplier<CrudRepository<Integer, CrudTestResource>> repository;
    private static CrudTestResource                                    resource1;
    private static CrudTestResource                                    resource2;
    private static CrudTestResource                                    resource3;

    private ResourceRetriever<Integer, CrudTestResource> retriever;

    @BeforeAll
    public static void beforeAll() throws Exception
    {
        repository = () -> new JpaCrudRepository<>(connection.getEntityManagerFactory(), Integer.class, "id", CrudTestResource.class);
        ResourceCreator<Integer, CrudTestResource> creator = new ResourceCreator<>(repository, null);

        resource1 = creator.create(() -> new CrudTestResource("name1", "email1"));
        resource2 = creator.create(() -> new CrudTestResource("name2", "email2"));
        resource3 = creator.create(() -> new CrudTestResource("name3", "email3"));
    }

    @BeforeEach
    public void beforeEach()
    {
        retriever = new ResourceRetriever<>(Integer.class, repository);
    }

    @AfterAll
    public static void afterAll()
    {
        connection.reset();
    }

    @Test
    void getAll()
    {
        List<CrudTestResource> all = retriever.getAll();

        assertNotNull(all);
        assertEquals(3, all.size());

        assertEquals(resource1, all.get(0));
        assertEquals(resource2, all.get(1));
        assertEquals(resource3, all.get(2));
    }

    @Test
    void count()
    {
        assertEquals(3, retriever.count());
    }

    @Test
    void getK() throws Exception
    {
        assertEquals(resource1, retriever.get(resource1.getId()));
    }

    @Test
    void getKThrowsResourceNotFoundException()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            retriever.get(-1);
        });
    }

    @Test
    void getKSet()
    {
        Set<Integer>                   toRetrieve = new HashSet<>(Arrays.asList(-1, resource2.getId(), resource3.getId()));
        Map<Integer, CrudTestResource> retrieved  = retriever.get(toRetrieve);

        assertEquals(2, retrieved.size());
        assertEquals(resource2, retrieved.get(resource2.getId()));
        assertEquals(resource3, retrieved.get(resource3.getId()));
    }

    @Test
    void existsK()
    {
        assertFalse(retriever.exists(-1));
        assertTrue(retriever.exists(resource1.getId()));
    }

    @Test
    void existsKSet()
    {
        Set<Integer> toRetrieve = new HashSet<>();
        assertTrue(retriever.exists(toRetrieve));

        toRetrieve.add(resource1.getId());
        assertTrue(retriever.exists(toRetrieve));

        toRetrieve.add(-1);
        assertFalse(retriever.exists(toRetrieve));
    }
}