package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.CrudRepository;
import com.tvestergaard.start.data.repositories.base.RepositoryEntity;
import com.tvestergaard.start.logic.crud.validation.ResourceValidationException;
import com.tvestergaard.start.logic.crud.validation.ResourceValidator;

import java.util.function.Supplier;

public class ResourceCreator<K extends Comparable<K>, E extends RepositoryEntity<K>>
{

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
     * @param repositoryFactory The factory that creates the repository to create.
     */
    public ResourceCreator(Supplier<CrudRepository<K, E>> repositoryFactory)
    {
        this(repositoryFactory, null);
    }

    /**
     * Creates a new {@link ResourceCreator}.
     *
     * @param repositoryFactory The factory that creates the repository to create.
     * @param validatorFactory  The factory that creates the validator that is used to validate the entity.
     */
    public ResourceCreator(Supplier<CrudRepository<K, E>> repositoryFactory, ValidatorFactory<E> validatorFactory)
    {
        this.repositoryFactory = repositoryFactory;
        this.validatorFactory = validatorFactory;
    }

    /**
     * Validates and persists an entity created from the provided resource data.
     *
     * @param data The data from which the entity is created.
     * @return The persisted entity.
     * @throws ResourceValidationException When the entity cannot be created.
     */
    public E persist(ResourceData<E> data) throws ResourceValidationException
    {
        E entity = data.toEntity();
        if (validatorFactory != null) {
            ResourceValidator<E> validator = validatorFactory.create(entity);
            validator.throwResourceValidationException();
        }

        try (CrudRepository<K, E> repository = repositoryFactory.get()) {
            repository.begin();
            entity = repository.persist(entity);
            repository.commit();
            return entity;
        }
    }
}
