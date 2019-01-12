package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepository;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceUpdaterTest
{


    private static CrudJpaTestConnection                               connection = CrudJpaTestConnection.getInstance();
    private static Supplier<CrudRepository<Integer, CrudTestResource>> repository;
    private static CrudTestResource                                    resource1;
    private static CrudTestResource                                    resource2;
    private static CrudTestResource                                    resource3;

    private ResourceUpdater<Integer, CrudTestResource> updater;

    private ResourceRetriever<Integer, CrudTestResource> newRetriever()
    {
        return new ResourceRetriever<>(Integer.class, repository);
    }


    @BeforeEach
    public void beforeEach() throws Exception
    {
        repository = () -> new JpaCrudRepository<>(connection.getEntityManagerFactory(), Integer.class, "id", CrudTestResource.class);
        ResourceCreator<Integer, CrudTestResource> creator = new ResourceCreator<>(repository, null);

        resource1 = creator.create(() -> new CrudTestResource("name1", "email1"));
        resource2 = creator.create(() -> new CrudTestResource("name2", "email2"));
        resource3 = creator.create(() -> new CrudTestResource("name3", "email3"));

        updater = new ResourceUpdater<>(Integer.class, CrudTestResource.class, repository, null);
    }

    @AfterEach
    public void afterAll()
    {
        connection.reset();
    }

    @Test
    void updateK() throws Exception
    {
        assertEquals(resource1, newRetriever().get(resource1.getId()));
        CrudTestResource updated = updater.update(resource1.getId(), () -> new CrudTestResource("name4", "email4"));

        assertEquals(resource1.getId(), updated.getId());
        assertEquals("name4", updated.getName());
        assertEquals("email4", updated.getEmail());
        assertEquals("name4", newRetriever().get(resource1.getId()).getName());
    }

    @Test
    void updateKThrowsResourceNotFoundException()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            updater.update(-1, () -> new CrudTestResource());
        });
    }

    @Test
    void updateR() throws Exception
    {
        assertEquals(resource1, newRetriever().get(resource1.getId()));
        CrudTestResource updated = updater.update(resource1, () -> new CrudTestResource("name4", "email4"));

        assertEquals(resource1.getId(), updated.getId());
        assertEquals("name4", updated.getName());
        assertEquals("email4", updated.getEmail());
        assertEquals("name4", newRetriever().get(resource1.getId()).getName());
    }

    @Test
    void updateRThrowsResourceNotFoundException()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            updater.update((CrudTestResource) null, CrudTestResource::new);
        });

        assertThrows(ResourceNotFoundException.class, () -> {
            updater.update(new CrudTestResource(), CrudTestResource::new);
        });
    }

    @Test
    void updateMany() throws Exception
    {
        assertTrue(updater.update(new HashMap<>()).isEmpty());

        Map<Integer, ResourceData<CrudTestResource>> map = new HashMap<>();
        map.put(resource1.getId(), () -> new CrudTestResource("name4", "email4"));
        map.put(resource2.getId(), () -> new CrudTestResource("name5", "email5"));

        assertEquals(resource1, newRetriever().get(resource1.getId()));
        assertEquals(resource2, newRetriever().get(resource2.getId()));

        Map<Integer, CrudTestResource> updated = updater.update(map);

        assertEquals(2, updated.size());

        assertEquals("name4", updated.get(resource1.getId()).getName());
        assertEquals("name4", newRetriever().get(resource1.getId()).getName());

        assertEquals("name5", updated.get(resource2.getId()).getName());
        assertEquals("name5", newRetriever().get(resource2.getId()).getName());
    }

    @Test
    void updateManyThrowsResourceNotFound()
    {
        assertThrows(ResourceNotFoundException.class, () -> {
            HashMap<Integer, ResourceData<CrudTestResource>> map = new HashMap<>();
            map.put(-1, CrudTestResource::new);
            updater.update(map);
        });
    }

    @Test
    public void updateManyRollback() throws Exception
    {
        // Initial
        assertEquals("name1", newRetriever().get(resource1.getId()).getName());
        assertEquals("name2", newRetriever().get(resource2.getId()).getName());

        assertThrows(ResourceNotFoundException.class, () -> {

            Map<Integer, ResourceData<CrudTestResource>> map = new HashMap<>();
            map.put(resource1.getId(), () -> new CrudTestResource("name4", "email4"));
            map.put(-1 /* Unknown key */, () -> new CrudTestResource("name5", "email5"));

            updater.update(map);
        });

        // No change
        assertEquals("name1", newRetriever().get(resource1.getId()).getName());
        assertEquals("name2", newRetriever().get(resource2.getId()).getName());
    }
}