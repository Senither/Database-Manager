package com.sendev.databasemanager.exceptions;

public class DatabaseException extends RuntimeException
{

    public DatabaseException(String message)
    {
        super(message);
    }

    public DatabaseException(String message, Exception e)
    {
        super(message, e);
    }
}
