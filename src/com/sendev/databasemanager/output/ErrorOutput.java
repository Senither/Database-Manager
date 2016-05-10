package com.sendev.databasemanager.output;

import com.sendev.databasemanager.contracts.DatabaseOutput;

public class ErrorOutput extends DatabaseOutput
{
    @Override
    public boolean info()
    {
        return true;
    }

    @Override
    public boolean warning()
    {
        return true;
    }

    @Override
    public boolean error()
    {
        return true;
    }
}
