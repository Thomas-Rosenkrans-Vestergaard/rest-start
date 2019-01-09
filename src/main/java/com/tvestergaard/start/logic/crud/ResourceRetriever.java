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

    private final Class<K>                       kClass;
    private final Supplier<ReadRepository<K, E>> repositoryFactory;

    public ResourceRetriever(Class<K> kClass, Supplier<ReadRepository<K, E>> repositoryFactory)
    {
        this.kClass = kClass;
        this.repositoryFactory = repositoryFactory;
    }

    public List<E> getAll()
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.getAll();
        }
    }

    public long count()
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.count();
        }
    }

    public E get(K id) throws ResourceNotFoundException
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            E entity = repository.get(id);
            if (entity == null)
                throw new ResourceNotFoundException(kClass, id);

            return entity;
        }
    }

    public Map<K, E> get(Set<K> ids)
    {
        try (ReadRepository<K, E> repository = repositoryFactory.get()) {
            return repository.get(ids);
        }
    }

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
