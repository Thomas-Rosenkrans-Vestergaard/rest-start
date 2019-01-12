package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepository;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceDeleterTest
{

    private CrudJpaTestConnection                               connection = CrudJpaTestConnection.getInstance();
    private Supplier<CrudRepository<Integer, CrudTestResource>> repository;
    private ResourceCreator<Integer, CrudTestResource>          creator;
    private ResourceRetriever<Integer, CrudTestResource>        retriever;
    private ResourceDeleter<Integer, CrudTestResource>          deleter;

    @BeforeEach
    public void beforeEach()
    {
        repository = () -> new JpaCrudRepository<>(connection.getEntityManagerFactory(), Integer.class, "id", CrudTestResource.class);
        creator = new ResourceCreator<>(repository, null);
        retriever = new ResourceRetriever<>(Integer.class, repository);
        deleter = new ResourceDeleter<>(Integer.class, repository);
    }

    @AfterEach
    public void afterEach()
    {
        connection.reset();
    }

    @Test
    void deleteK() throws Exception
    {
        CrudTestResource created = creator.create(() -> new CrudTestResource("name1", "email1"));
        assertTrue(retriever.exists(created.getId()));
        assertEquals(created, deleter.delete(created.getId()));
        assertFalse(retriever.exists(created.getId()));
    }

    @Test
    void deleteKThrowsResourceNotFoundException()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            deleter.delete(1);
        });
    }

    @Test
    void deleteR() throws Exception
    {
        CrudTestResource created = creator.create(() -> new CrudTestResource("name1", "email1"));
        assertTrue(retriever.exists(created.getId()));
        assertEquals(created, deleter.delete(created));
        assertFalse(retriever.exists(created.getId()));
    }

    @Test
    void deleteRThrowsResourceNotFoundException()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            deleter.delete(new CrudTestResource("name1", "email1"));
        });
    }

    @Test
    void deleteKCollection() throws Exception
    {
        CrudTestResource resource1 = new CrudTestResource("name1", "email1");
        CrudTestResource resource2 = new CrudTestResource("name2", "email2");
        CrudTestResource resource3 = new CrudTestResource("name3", "email3");

        List<CrudTestResource> created = creator.create(Arrays.asList(
                () -> resource1,
                () -> resource2,
                () -> resource3
        ));

        assertEquals(3, retriever.count());

        Map<Integer, CrudTestResource> deleted = deleter.delete(created.stream().map(CrudTestResource::getId).collect(Collectors.toList()));

        assertEquals(3, deleted.size());
        assertEquals(resource1, deleted.get(resource1.getId()));
        assertEquals(resource2, deleted.get(resource2.getId()));
        assertEquals(resource3, deleted.get(resource3.getId()));

        assertEquals(0, retriever.count());
    }

    @Test
    void deleteRList() throws Exception
    {

        CrudTestResource resource1 = new CrudTestResource("name1", "email1");
        CrudTestResource resource2 = new CrudTestResource("name2", "email2");
        CrudTestResource resource3 = new CrudTestResource("name3", "email3");

        List<CrudTestResource> created = creator.create(Arrays.asList(
                () -> resource1,
                () -> resource2,
                () -> resource3
        ));

        assertEquals(3, retriever.count());

        List<CrudTestResource> deleted = deleter.delete(created);

        assertEquals(3, deleted.size());
        assertEquals(resource1, deleted.get(0));
        assertEquals(resource2, deleted.get(1));
        assertEquals(resource3, deleted.get(2));

        assertEquals(0, retriever.count());
    }
}