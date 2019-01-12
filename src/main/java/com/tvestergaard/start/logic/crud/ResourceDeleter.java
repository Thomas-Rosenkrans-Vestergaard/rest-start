package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.ResourceNotFoundException;

import java.util.*;
import java.util.function.Supplier;

/**
 * Deletes resources from some repository.
 *
 * @param <K> The type of the keys of the resources.
 * @param <R> The resource type.
 */
public class ResourceDeleter<K extends Comparable<K>, R extends RepositoryEntity<K>>
{

    /**
     * The class of the key type.
     */
    private final Class<K> kClass;

    /**
     * The factory that creates the repository to create.
     */
    private final Supplier<CrudRepository<K, R>> repositoryFactory;

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param kClass            The class of the key type.
     * @param repositoryFactory The factory that creates the repository to create.
     */
    public ResourceDeleter(Class<K> kClass, Supplier<CrudRepository<K, R>> repositoryFactory)
    {
        this.kClass = kClass;
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Deletes the resource with the provided key.
     *
     * @param id The id of the resource to delete.
     * @return The deleted resource.
     * @throws ResourceNotFoundException When an resource with the provided id does not exist.
     */
    public R delete(K id) throws ResourceNotFoundException
    {
        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            R deleted = repository.delete(id);
            if (deleted == null)
                throw new ResourceNotFoundException(kClass, id);
            repository.commit();
            return deleted;
        }
    }

    /**
     * Deletes the provided resource from persistent storage.
     *
     * @param resource The resource to delete from persistent storage.
     * @return The deleted resource.
     * @throws ResourceNotFoundException When the provided resource does not exist in persistent storage, or the
     *                                   provided {@code resource} equals {@code null}.
     */
    public R delete(R resource) throws ResourceNotFoundException
    {
        if (resource == null || resource.getId() == null)
            throw new ResourceNotFoundException(kClass, "null");

        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            R deleted = repository.delete(resource);
            if (deleted == null)
                throw new ResourceNotFoundException(kClass, resource.getId());
            repository.commit();
            return deleted;
        }
    }

    /**
     * Deletes the entities with the provided keys.
     *
     * @param keys The keys to delete from persistent storage.
     * @return A map containing the deleted entities mapped to their keys. Any non-existing keys
     * are not returned in the map.
     */
    public Map<K, R> delete(Collection<K> keys)
    {
        if (keys == null || keys.isEmpty())
            return new HashMap<>(0);

        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            Map<K, R> deleted = repository.delete(keys);
            repository.commit();
            return deleted;
        }
    }

    /**
     * Deletes the provided resource from persistent storage.
     *
     * @param entities The entities to delete from persistence storage.
     * @return The list of the entities to delete.
     */
    public List<R> delete(List<R> entities)
    {
        if (entities == null || entities.isEmpty())
            return new ArrayList<>(0);

        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            List<R> deleted = repository.delete(entities);
            repository.commit();
            return deleted;
        }
    }
}
