package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import com.tvestergaard.start.logic.crud.validation.ResourceValidationException;
import com.tvestergaard.start.logic.crud.validation.ResourceValidator;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Updates resources in some repository.
 *
 * @param <K> The type of the keys of the resources.
 * @param <R> The resource type.
 */
public class ResourceUpdater<K extends Comparable<K>, R extends RepositoryEntity<K>>
{

    /**
     * The class of the resource key type.
     */
    private final Class<K> kClass;

    /**
     * The class of the resource type.
     */
    private final Class<R> rClass;

    /**
     * The factory that creates the repository to create.
     */
    private final Supplier<CrudRepository<K, R>> repositoryFactory;

    /**
     * The factory that creates the validator that is used to validate the resource.
     */
    private final ValidatorFactory<R> validatorFactory;

    /**
     * Creates a new {@link ResourceUpdater}.
     *
     * @param kClass            The class of the resource key type.
     * @param rClass            The class of the resource type.
     * @param repositoryFactory The factory that creates the repository to create.
     * @param validatorFactory  The factory that creates the validator that is used to validate the resource.
     */
    public ResourceUpdater(Class<K> kClass, Class<R> rClass, Supplier<CrudRepository<K, R>> repositoryFactory, ValidatorFactory<R> validatorFactory)
    {
        this.kClass = kClass;
        this.rClass = rClass;
        this.repositoryFactory = repositoryFactory;
        this.validatorFactory = validatorFactory;
    }

    /**
     * Updates the resource with the provided {@code key}, to match the provided {@code data}.
     *
     * @param key  The key of the resource to update.
     * @param data The data to update the resource to.
     * @return The updated resource.
     * @throws ResourceNotFoundException      When the resource with the provided key does not exist.
     * @throws MalformedResourceDataException When the resource cannot be created from the resource data.
     * @throws ResourceValidationException    When the resource cannot be validated.
     */
    public R update(K key, ResourceData<R> data)
            throws
            ResourceNotFoundException,
            MalformedResourceDataException,
            ResourceValidationException
    {
        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            R found = repository.get(key);
            if (found == null)
                throw new ResourceNotFoundException(kClass, key == null ? "null" : key);

            R sent   = data.toResource();
            R merged = merge(sent, found);
            if (validatorFactory != null) {
                ResourceValidator<R> validator = validatorFactory.create(merged);
                validator.throwResourceValidationException();
            }

            repository.begin();
            R updated = repository.update(merged);
            repository.commit();

            return updated;
        }
    }

    /**
     * Updates the provided {@code resource} to match the provided {@code data}.
     *
     * @param resource The resource to update.
     * @param data     The data to update the resource to.
     * @return The updated resource.
     * @throws ResourceNotFoundException      When the resource with the provided key does not exist.
     * @throws MalformedResourceDataException When the resource cannot be created from the resource data.
     * @throws ResourceValidationException    When the resource cannot be validated.
     */
    public R update(R resource, ResourceData<R> data)
            throws
            ResourceNotFoundException,
            MalformedResourceDataException,
            ResourceValidationException
    {
        if (resource == null)
            return update((K) null, data);

        return update(resource.getId(), data);
    }

    /**
     * Updates all the resource with the provided ids, to match the provided resource data.
     *
     * @param updateData The new data mapped to the resource key to update.
     * @return The updated resources mapped to their id.
     * @throws ResourceNotFoundException      When the resource with the provided key does not exist.
     * @throws MalformedResourceDataException When the resource cannot be created from the resource data.
     * @throws ResourceValidationException    When the resource cannot be validated.
     */
    public Map<K, R> update(Map<K, ResourceData<R>> updateData)
            throws
            ResourceNotFoundException,
            MalformedResourceDataException,
            ResourceValidationException
    {

        Map<K, R> results = new HashMap<>(updateData.size());
        try (CrudRepository<K, R> repository = repositoryFactory.get()) {

            repository.begin();
            for (Map.Entry<K, ResourceData<R>> entry : updateData.entrySet()) {
                K key   = entry.getKey();
                R found = repository.get(key);
                if (found == null)
                    throw new ResourceNotFoundException(kClass, key);

                R sent   = entry.getValue().toResource();
                R merged = merge(sent, found);
                if (validatorFactory != null) {
                    ResourceValidator<R> validator = validatorFactory.create(merged);
                    validator.throwResourceValidationException();
                }

                results.put(key, repository.update(merged));
            }

            repository.commit();
        }

        return results;
    }

    /**
     * Transfers all non-null values in the provided {@code source} resource to the provided {@code destination}
     * resource.
     *
     * @param source      The source resource.
     * @param destination The destination resource.
     * @return The updated source resource.
     */
    private R merge(R source, R destination)
    {
        try {
            for (Field field : destination.getClass().getDeclaredFields()) {

                if (field.getAnnotation(Id.class) != null)
                    continue;

                boolean accessible = field.isAccessible();
                field.setAccessible(true);

                Object value = field.get(source);
                if (value != null)
                    field.set(destination, value);

                field.setAccessible(accessible);
            }

            return destination;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
