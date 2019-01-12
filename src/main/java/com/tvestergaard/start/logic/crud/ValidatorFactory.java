package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.logic.crud.validation.ResourceValidator;

@FunctionalInterface
public interface ValidatorFactory<R>
{

    ResourceValidator<R> create(R resource);
}
