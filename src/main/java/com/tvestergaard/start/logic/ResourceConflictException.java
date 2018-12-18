package com.tvestergaard.start.logic;

/**
 * Thrown when a new record cannot be created, because the new record is in conflict with an
 * existing record. An example could be trying to create a user with an email that already
 * exists in the system.
 */
public class ResourceConflictException extends LogicException
{

    /**
     * Creates a new {@link ResourceConflictException}.
     *
     * @param resource     The type of resource that was in conflict.
     * @param errorMessage The error message.
     */
    public ResourceConflictException(Class resource, String errorMessage)
    {
        super(String.format("%sConflictException", resource.getSimpleName()), errorMessage, null);
    }
}
