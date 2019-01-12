package com.tvestergaard.start.logic.crud;

@FunctionalInterface
public interface ResourceData<R>
{

    /**
     * Converts the data to an resource.
     *
     * @return The resulting resource.
     * @throws MalformedResourceDataException When the data cannot be converted into a resource.
     */
    R toResource() throws MalformedResourceDataException;
}
