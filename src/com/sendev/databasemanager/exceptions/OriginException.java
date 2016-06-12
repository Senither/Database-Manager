package com.sendev.databasemanager.exceptions;

public class OriginException extends RuntimeException
{
    public OriginException()
    {
        super("Origin exception has been thrown, if you're seeing this in the console something went wrong!");
    }
}
