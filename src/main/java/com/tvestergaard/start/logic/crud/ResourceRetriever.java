package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.ReadRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ResourceRetriever<K extends Comparable<K>, E extends RepositoryEntity<K>>
{

    /**
     * The class of the key type.
     */
    private final Class<K> kClass;

    /**
     * The factory that produces repository instances.
     */
    private final Supplier<ReadRepository<K, E>> repositoryFactory;

    /**
     * Creates a new {@link ResourceRetriever}.
     *
     * @param kClass            The class of the key type.
     * @param repositoryFactory The factory that produces repository instances.
     */
    public ResourceRetriever(Class<K> kClass, Supplier<ReadRepository<K, E>> repositoryFactory)
    {
        this.kClass = kClass;
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Returns all the entities in persistent storage.
     *
     * @return The list of the entities.
     */
    public List<E> getAll()
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
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
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
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
    public E get(K id) throws ResourceNotFoundException
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            E resource = repository.get(id);
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
    public Map<K, E> get(Set<K> ids)
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.get(ids);
        }
    }

    /**
     * Checks if the resource with the provided id exists.
     *
     * @param id The id of the resource to check for.
     * @return {@c}
     */
    public boolean exists(K id)
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.exists(id);
        }
    }

    public boolean exists(Set<K> ids)
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.exists(ids);
        }
    }
}
