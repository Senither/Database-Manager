package com.sendev.databasemanager;

import com.sendev.databasemanager.contracts.Database;

public class Connection
{
    private final ConnectionLevel level;
    private final Database connection;

    /**
     * Creates a database connection for the DBM connection container, allowing the
     * container to sort and managed the connection from it's connection level.
     *
     * @param dbm        DatabaseManager instance.
     * @param level      The connection level/priority of the given connection.
     * @param connection The database connection instance.
     */
    public Connection(DatabaseManager dbm, ConnectionLevel level, Database connection)
    {
        this.level = level;
        this.connection = connection;

        this.connection.setDatabaseManager(dbm);
    }

    /**
     * Gets the connection level/priority.
     *
     * @return The connection level/priority.
     */
    public ConnectionLevel getLevel()
    {
        return level;
    }

    /**
     * Gets the database connection instance.
     *
     * @return The database connection instance.
     */
    public Database getConnection()
    {
        return connection;
    }
}
