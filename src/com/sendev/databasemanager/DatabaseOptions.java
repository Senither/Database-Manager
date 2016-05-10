package com.sendev.databasemanager;

import com.sendev.databasemanager.schema.DatabaseEngine;

public final class DatabaseOptions
{
    private DatabaseEngine engine = null;
    private int queryTimeout = -1;
    private int queryReturnLimit = -1;
    private boolean debug;

    public void setDefaultEngine(DatabaseEngine engine)
    {
        this.engine = engine;
    }

    public DatabaseEngine getEngine()
    {
        return engine;
    }

    public void setQueryTimeout(int queryTimeout)
    {
        this.queryTimeout = queryTimeout;
    }

    public int getQueryTimeout()
    {
        return queryTimeout;
    }

    public void setQueryReturnLimit(int queryReturnLimit)
    {
        this.queryReturnLimit = queryReturnLimit;
    }

    public int getQueryReturnLimit()
    {
        return queryReturnLimit;
    }

    public void enableDebugMessages()
    {
        this.debug = true;
    }

    public void disableDebugMessages()
    {
        this.debug = false;
    }

    public boolean isDebugMessagesEnabled()
    {
        return debug;
    }
}
