package com.sendev.databasemanager;

import com.sendev.databasemanager.schema.DatabaseEngine;

public final class DatabaseOptions
{
    private DatabaseEngine engine = null;
    private int queryTimeout = -1;
    private int queryReturnLimit = -1;
    private boolean debug;
    private String prefix = "";

    /**
     * Sets the default database engine that should be used when inserting and creating data.
     * <p>
     * <strong>Note:</strong> It is not recommended that you change this from
     * the default(<code>InnoDB</code>) unless you know what you're doing.
     *
     * @param engine a database engine enumeration instance.
     */
    public void setDefaultEngine(DatabaseEngine engine)
    {
        this.engine = engine;
    }

    /**
     * Gets the database engine that should be used with <code>INSERT</code> and <code>CREATE</code> statements.
     *
     * @return The database engine that should be used.
     */
    public DatabaseEngine getEngine()
    {
        return engine;
    }

    /**
     * Sets the default timeout for all queries in milliseconds, any query that
     * takes longer then the specified time will throw an SQLTimeoutException.
     * <p>
     * <strong>Note:</strong> Setting the query timeout to -1 will use the
     * current database connections default fallback timeout value, which
     * will vary depending on the database type that is being used.
     *
     * @see java.sql.SQLTimeoutException
     *
     * @param queryTimeout the timeout in milliseconds.
     */
    public void setQueryTimeout(int queryTimeout)
    {
        this.queryTimeout = queryTimeout;
    }

    /**
     * Gets the query timeout length.
     *
     * @return The timeout length.
     */
    public int getQueryTimeout()
    {
        return queryTimeout;
    }

    /**
     * Sets the query return limit, forcing <code>SELECT</code>
     * statements to not return more than the given amount.
     * <p>
     * <strong>Note:</strong> The executed query will still fetch as many rows as it can, if the returned
     * rows exceeds the return limit it will just cut off any rows that exceeded the limit, because of
     * this it is recommend that you use the SQL <code>LIMIT</code> statement, or something
     * equivalent to it when you want a limited amount of results back.
     * <p>
     * <strong>Note:</strong> Setting the query return limit to -1 will return it to the default settings,
     * of just fetching as much data as it can with no limit, unless one is specified in the query.
     *
     * @param queryReturnLimit The amount to limit <code>SELECT</code> statements returned results to.
     */
    public void setQueryReturnLimit(int queryReturnLimit)
    {
        this.queryReturnLimit = queryReturnLimit;
    }

    /**
     * Gets the query return limit.
     *
     * @return The query return limit.
     */
    public int getQueryReturnLimit()
    {
        return queryReturnLimit;
    }

    public void setPrefix(String prefix)
    {
        if (prefix == null) {
            prefix = "";
        }

        this.prefix = prefix.trim();
    }

    public String getPrefix()
    {
        return prefix;
    }

    /**
     * This will enable the debug mode in the Database Manager, allowing the Database
     * Output to give more detailed information about what is going on within the
     * DBM, internal exceptions will also be printed out to the console.
     * <p>
     * <strong>Note:</strong> There are no reason to enable this on production servers.
     */
    public void enableDebugMessages()
    {
        this.debug = true;
    }

    /**
     * This will disable the debug mode in the Database Manager.
     */
    public void disableDebugMessages()
    {
        this.debug = false;
    }

    /**
     * Checks to see if the debugging mode is enabled within the DBM.
     *
     * @return true if debugging is enabled, otherwise it will return false.
     */
    public boolean isDebugMessagesEnabled()
    {
        return debug;
    }
}
