package com.tvestergaard.start.logic;

public class LogicException extends Exception
{

    private final String    errorName;
    private final String    errorMessage;

    public LogicException(String errorName, String errorMessage, Throwable cause)
    {
        super(cause);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    public String getErrorName()
    {
        return this.errorName;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
