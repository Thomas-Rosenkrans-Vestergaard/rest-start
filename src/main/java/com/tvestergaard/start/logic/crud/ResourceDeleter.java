package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.ResourceNotFoundException;

import java.util.*;
import java.util.function.Supplier;

public class ResourceDeleter<K extends Comparable<K>, E extends RepositoryEntity<K>>
{

    /**
     * The class of the key type.
     */
    private final Class<K> kClass;

    /**
     * The factory that creates the repository to create.
     */
    private final Supplier<CrudRepository<K, E>> repositoryFactory;

    /**
     * The factory that creates the validator that is used to validate the entity.
     */
    private final ValidatorFactory<E> validatorFactory;

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param kClass            The class of the key type.
     * @param repositoryFactory The factory that creates the repository to create.
     */
    public ResourceDeleter(Class<K> kClass, Supplier<CrudRepository<K, E>> repositoryFactory)
    {
        this(kClass, repositoryFactory, null);
    }

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param kClass            The class of the key type.
     * @param repositoryFactory The factory that creates the repository to create.
     * @param validatorFactory  The factory that creates the validator that is used to validate the entity.
     */
    public ResourceDeleter(Class<K> kClass, Supplier<CrudRepository<K, E>> repositoryFactory, ValidatorFactory<E> validatorFactory)
    {
        this.kClass = kClass;
        this.repositoryFactory = repositoryFactory;
        this.validatorFactory = validatorFactory;
    }

    /**
     * Deletes the entity with the provided key.
     *
     * @param id The id of the entity to delete.
     * @return The deleted entity.
     * @throws ResourceNotFoundException When an entity with the provided id does not exist.
     */
    public E delete(K id) throws ResourceNotFoundException
    {
        try (CrudRepository<K, E> repository = repositoryFactory.get()) {
            repository.begin();
            E deleted = repository.delete(id);
            if (deleted == null)
                throw new ResourceNotFoundException(kClass, id);
            repository.commit();
            return deleted;
        }
    }

    /**
     * Deletes the provided entity from persistent storage.
     *
     * @param entity The entity to delete from persistent storage.
     * @return The deleted entity.
     * @throws ResourceNotFoundException When the provided entity does not exist in persistent storage, or the
     *                                   provided {@code entity} equals {@code null}.
     */
    public E delete(E entity) throws ResourceNotFoundException
    {
        if (entity == null)
            throw new ResourceNotFoundException(kClass, null);

        try (CrudRepository<K, E> repository = repositoryFactory.get()) {
            repository.begin();
            E deleted = repository.delete(entity);
            if (deleted == null)
                throw new ResourceNotFoundException(kClass, entity.getId());
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
    public Map<K, E> delete(Collection<K> keys)
    {
        if (keys == null || keys.isEmpty())
            return new HashMap<>(0);

        try (CrudRepository<K, E> repository = repositoryFactory.get()) {
            repository.begin();
            Map<K, E> deleted = repository.delete(keys);
            repository.commit();
            return deleted;
        }
    }

    /**
     * Deletes the provided entity from persistent storage.
     *
     * @param entities The entities to delete from persistence storage.
     * @return The list of the entities to delete.
     */
    public List<E> delete(List<E> entities)
    {
        if (entities == null || entities.isEmpty())
            return new ArrayList<>(0);

        try (CrudRepository<K, E> repository = repositoryFactory.get()) {
            repository.begin();
            List<E> deleted = repository.delete(entities);
            repository.commit();
            return deleted;
        }
    }
}
