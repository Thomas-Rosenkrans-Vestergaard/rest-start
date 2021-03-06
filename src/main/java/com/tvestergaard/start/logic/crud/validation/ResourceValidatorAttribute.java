package com.tvestergaard.start.logic.crud.validation;

public interface ResourceValidatorAttribute<R, V>
{

    /**
     * Performs the check created from the provided constructor.
     *
     * @param constructor The constructor that creates the check to perform.
     * @return this
     */
    ResourceValidatorAttribute<R, V> check(ResourceValidatorAttributeCheckConstructor<R, V> constructor);
}
