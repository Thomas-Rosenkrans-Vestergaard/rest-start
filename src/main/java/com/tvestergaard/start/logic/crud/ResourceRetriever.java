package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.ReadRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Retrieves resources from some repository.
 *
 * @param <K> The type of the keys of the resources.
 * @param <R> The resource type.
 */
public class ResourceRetriever<K extends Comparable<K>, R extends RepositoryEntity<K>>
{

    /**
     * The class of the key type.
     */
    private final Class<K> kClass;

    /**
     * The factory that produces repository instances.
     */
    private final Supplier<ReadRepository<K, R>> repositoryFactory;

    /**
     * Creates a new {@link ResourceRetriever}.
     *
     * @param kClass            The class of the key type.
     * @param repositoryFactory The factory that produces repository instances.
     */
    public ResourceRetriever(Class<K> kClass, Supplier<ReadRepository<K, R>> repositoryFactory)
    {
        this.kClass = kClass;
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Returns all the entities in persistent storage.
     *
     * @return The list of the entities.
     */
    public List<R> getAll()
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            return repository.getAll();
        }
    }

    /**
     * Returns the number of entities in persistent storage.
     *
     * @return The number of entities in persistent storage.
     */
    public long count()
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            return repository.count();
        }
    }

    /**
     * Returns the resource with the provided id.
     *
     * @param id The id of the resource to return.
     * @return The resource with the provided id.
     * @throws ResourceNotFoundException When no such resource exists.
     */
    public R get(K id) throws ResourceNotFoundException
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            R resource = repository.get(id);
            if (resource == null)
                throw new ResourceNotFoundException(kClass, id);

            return resource;
        }
    }

    /**
     * Returns the resources with the provided ids.
     * Does not raise an exception when the ids does not exist.
     *
     * @param ids The ids of the resources to return.
     * @return The resources with the provided ids.
     */
    public Map<K, R> get(Set<K> ids)
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            return repository.get(ids);
        }
    }

    /**
     * Checks if the resource with the provided id exists.
     *
     * @param id The id of the resource to check for.
     * @return {@code true} when the resource exists, {@code false} when it does not.
     */
    public boolean exists(K id)
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            return repository.exists(id);
        }
    }

    /**
     * Checks if all the resources with the provided ids exists.
     *
     * @param ids The resource ids to check for.
     * @return {@code true} when all the resources with the provided ids exists, {@code false} otherwise.
     */
    public boolean exists(Set<K> ids)
    {
        try (ReadRepository<K, R> repository = repositoryFactory.get()) {
            return repository.exists(ids);
        }
    }
}
