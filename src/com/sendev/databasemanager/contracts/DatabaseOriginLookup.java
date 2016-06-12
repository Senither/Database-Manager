package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.exceptions.OriginException;

public interface DatabaseOriginLookup
{

    public void throwsOriginException() throws OriginException;
}
