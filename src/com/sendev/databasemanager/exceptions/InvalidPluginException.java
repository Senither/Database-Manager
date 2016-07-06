package com.sendev.databasemanager.exceptions;

public class InvalidPluginException extends RuntimeException
{

    public InvalidPluginException(String message)
    {
        super(message);
    }

    public InvalidPluginException(String message, Exception e)
    {
        super(message, e);
    }
}
