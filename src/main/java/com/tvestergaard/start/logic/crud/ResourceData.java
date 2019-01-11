package com.tvestergaard.start.logic.crud;

public interface ResourceData<E>
{

    /**
     * Converts the data to an entity.
     *
     * @return The resulting entity.
     * @throws MalformedResourceDataException When the data cannot be converted into a resource.
     */
    E toEntity() throws MalformedResourceDataException;
}
