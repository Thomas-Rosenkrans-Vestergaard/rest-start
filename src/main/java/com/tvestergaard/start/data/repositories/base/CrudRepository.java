package com.tvestergaard.start.data.repositories.base;

import com.tvestergaard.start.data.repositories.base.transactions.TransactionalRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An interface defining common read and write operations on some {@code RepositoryEntity}.
 *
 * @param <E> The type of the entity managed by the repository.
 * @param <K> The type of the key of the entities managed by the repository.
 */
public interface CrudRepository<K extends Comparable<K>, E extends RepositoryEntity<K>>
        extends ReadRepository<K, E>, TransactionalRepository
{

    /**
     * Persists the provided entity,
     *
     * @param entity The entity to persist.
     * @return The persisted entity.
     */
    E persist(E entity);

    /**
     * Forces the entity to update.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     */
    E update(E entity);

    /**
     * Deletes the entity with the provided id.
     *
     * @param id The id of the entity to delete.
     * @return The deleted entity, or {@code null} when no entity was deleted.
     */
    E delete(K id);

    /**
     * Deletes the provided entity.
     *
     * @param entity The entity to delete.
     * @return The deleted entity.
     */
    E delete(E entity);

    /**
     * Deletes the entities with keys matching one of the provided keys.
     *
     * @param keys The keys of the entities to delete.
     * @return The map of the deleted entities. The key of the deleted entity is mapped to its id.
     */
    Map<K, E> delete(Collection<K> keys);

    /**
     * Deletes all the provided entities.
     *
     * @param entities The entities to delete from the repository.
     * @return A list with the updated entities.
     */
    List<E> delete(List<E> entities);
}
