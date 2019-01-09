package com.tvestergaard.start.logic.crud;

public interface ResourceData<E>
{

    /**
     * Converts the data to an entity.
     *
     * @return The resulting entity.
     */
    E toEntity();
}
