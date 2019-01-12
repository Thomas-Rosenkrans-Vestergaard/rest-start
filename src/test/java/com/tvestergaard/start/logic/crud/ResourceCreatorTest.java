package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceCreatorTest
{

    private CrudJpaTestConnection                               connection = CrudJpaTestConnection.getInstance();
    private Supplier<CrudRepository<Integer, CrudTestResource>> repository;
    private ResourceCreator<Integer, CrudTestResource>          instance;

    @BeforeEach
    public void beforeEach()
    {
        repository = () -> new JpaCrudRepository<>(connection.getEntityManagerFactory(), Integer.class, "id", CrudTestResource.class);
        instance = new ResourceCreator<>(repository, null);
    }

    @AfterEach
    public void afterEach()
    {
        connection.reset();
    }

    @Test
    void createR() throws Exception
    {
        CrudTestResource created = instance.create(() -> new CrudTestResource("name1", "email1"));
        assertEquals("name1", created.getName());
        assertEquals("email1", created.getEmail());
        assertNotNull(created.getId());
    }

    @Test
    void createRList() throws Exception
    {
        List<ResourceData<CrudTestResource>> resources = Arrays.asList(
                () -> new CrudTestResource("name1", "email1"),
                () -> new CrudTestResource("name2", "email2"),
                () -> new CrudTestResource("name3", "email3")
        );

        List<CrudTestResource> created = instance.create(resources);

        assertNotNull(created.get(0).getId());
        assertNotNull(created.get(1).getId());
        assertNotNull(created.get(2).getId());

        assertEquals("name1", created.get(0).getName());
        assertEquals("name2", created.get(1).getName());
        assertEquals("name3", created.get(2).getName());
    }
}