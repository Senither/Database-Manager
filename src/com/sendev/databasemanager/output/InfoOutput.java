package com.sendev.databasemanager.output;

import com.sendev.databasemanager.contracts.DatabaseOutput;

public class InfoOutput extends DatabaseOutput
{

    @Override
    public boolean info()
    {
        return true;
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
