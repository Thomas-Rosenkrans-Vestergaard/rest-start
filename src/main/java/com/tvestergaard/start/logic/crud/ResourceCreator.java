package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.crud.validation.ResourceValidationException;
import com.tvestergaard.start.logic.crud.validation.ResourceValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Inserts resources into some repository.
 *
 * @param <K> The type of the keys of the resources.
 * @param <R> The resource type.
 */
public class ResourceCreator<K extends Comparable<K>, R extends RepositoryEntity<K>>
{

    /**
     * The factory that creates the repository to create.
     */
    private final Supplier<CrudRepository<K, R>> repositoryFactory;

    /**
     * The factory that creates the validator that is used to validate the resource.
     */
    private final ValidatorFactory<R> validatorFactory;

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param repositoryFactory The factory that creates the repository to create.
     */
    public ResourceCreator(Supplier<CrudRepository<K, R>> repositoryFactory)
    {
        this(repositoryFactory, null);
    }

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param repositoryFactory The factory that creates the repository to create.
     * @param validatorFactory  The factory that creates the validator that is used to validate the resource.
     */
    public ResourceCreator(Supplier<CrudRepository<K, R>> repositoryFactory, ValidatorFactory<R> validatorFactory)
    {
        this.repositoryFactory = repositoryFactory;
        this.validatorFactory = validatorFactory;
    }

    /**
     * Validates and persists an resource created from the provided resource data.
     *
     * @param data The data from which the resource is created.
     * @return The persisted resource.
     * @throws MalformedResourceDataException When the resource cannot be created from the resource data.
     * @throws ResourceValidationException    When the resource cannot be validated.
     */
    public R create(ResourceData<R> data) throws MalformedResourceDataException, ResourceValidationException
    {
        R resource = data.toResource();
        if (validatorFactory != null) {
            ResourceValidator<R> validator = validatorFactory.create(resource);
            validator.throwResourceValidationException();
        }

        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            resource = repository.persist(resource);
            repository.commit();
            return resource;
        }
    }


    /**
     * Validates and persists resources created from the provided resource data.
     * <p>
     * When the validation or insertion of a single resource fails, all other insertions also fail.
     *
     * @param all The data from which the resources is created.
     * @return The persisted resources.
     * @throws MalformedResourceDataException When the resource cannot be created from the resource data.
     * @throws ResourceValidationException    When the resource cannot be validated.
     */
    public List<R> create(List<ResourceData<R>> all) throws MalformedResourceDataException, ResourceValidationException
    {

        List<R> resources = new ArrayList<>(all.size());

        if (validatorFactory != null) {
            for (ResourceData<R> data : all) {
                R                    resource  = data.toResource();
                ResourceValidator<R> validator = validatorFactory.create(resource);
                validator.throwResourceValidationException();
                resources.add(resource);
            }
        }

        try (CrudRepository<K, R> repository = repositoryFactory.get()) {
            repository.begin();
            resources = resources.stream().map(repository::persist).collect(Collectors.toList());
            repository.commit();
        }

        return resources;
    }
}
