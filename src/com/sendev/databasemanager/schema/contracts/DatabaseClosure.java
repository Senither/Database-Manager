package com.sendev.databasemanager.schema.contracts;

import com.sendev.databasemanager.schema.Blueprint;

public interface DatabaseClosure
{
    public void run(Blueprint table);
}
