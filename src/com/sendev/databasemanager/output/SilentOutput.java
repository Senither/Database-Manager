package com.sendev.databasemanager.output;

import com.sendev.databasemanager.contracts.DatabaseOutput;

public class SilentOutput extends DatabaseOutput
{

    @Override
    public boolean info()
    {
        return false;
    }

    @Override
    public boolean warning()
    {
        return false;
    }

    @Override
    public boolean error()
    {
        return false;
    }
}
