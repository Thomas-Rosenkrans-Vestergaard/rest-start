package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.logic.LogicException;

public class MalformedResourceDataException extends LogicException
{

    public MalformedResourceDataException(String errorMessage)
    {
        this(errorMessage, null);
    }

    public MalformedResourceDataException(String errorMessage, Throwable cause)
    {
        super("MalformedResourceData", errorMessage, cause);
    }
}
