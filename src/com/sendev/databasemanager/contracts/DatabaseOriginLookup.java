package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.exceptions.OriginException;

public interface DatabaseOriginLookup
{

    /**
     * Throws an origin exception for the origin lookup, no real logic should be placed inside this method.
     *
     * @throws OriginException the exception should be thrown right as the method are called
     */
    public void throwsOriginException() throws OriginException;
}
