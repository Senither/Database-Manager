package com.sendev.databasemanager.exceptions;

public class OriginException extends RuntimeException
{

    /**
     * Origin exception, this is used internally for origin lookups by the 
     * {@link com.sendev.databasemanager.contracts.DatabaseOriginLookup DatabaseOriginLookup contract}.
     */
    public OriginException()
    {
        super("Origin exception has been thrown, if you're seeing this in the console something went wrong!");
    }
}
