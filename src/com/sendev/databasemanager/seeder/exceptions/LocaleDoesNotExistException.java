package com.sendev.databasemanager.seeder.exceptions;

public class LocaleDoesNotExistException extends RuntimeException
{
    public LocaleDoesNotExistException(String message)
    {
        super(message);
    }
}
