package com.sendev.databasemanager;

import com.sendev.databasemanager.contracts.Database;
import java.util.HashMap;
import java.util.Map;

public class ConnectionContainer
{
    private final DatabaseManager dbm;
    private final Map<String, Connection> connections = new HashMap<>();
    private String defaultConnection = null;

    /**
     * Creates a new database connection container instance, making it easier to
     * store, manage and use the database connections parsed to the Database Manager.
     *
     * @see com.sendev.databasemanager.DatabaseManager
     *
     * @param dbm
     */
    public ConnectionContainer(DatabaseManager dbm)
    {
        this.dbm = dbm;
    }

    /**
     * Adds a database connection to the connections container, allowing
     * queries to be run against the connection.
     * <p>
     * The <code>connection level</code> will be used to determine the priority of the connection,
     * the top level will always be <code>DEFAULT</code>, after that the it's
     * <code>HIGEST</code>, <code>HIGE</code>, <code>MEDIUM</code>, etc...
     *
     * @param name       The name of the connection.
     * @param level      The level/priority of the connection.
     * @param connection The database instance, this can be MySQL, SQLite, etc...
     *
     * @return <code>True</code> if the connection was added successfully, or
     *         <code>False</code> if the connection name already exists.
     */
    public boolean addConnection(String name, ConnectionLevel level, Database connection)
    {
        name = name.toLowerCase();

        if (connections.containsKey(name)) {
            return false;
        }

        if (level.equals(ConnectionLevel.DEFAULT)) {
            defaultConnection = name;
        }

        connections.put(name, new Connection(dbm, level, connection));

        return true;
    }

    /**
     * Gets the database connection associated with the given name, if there isn't any database
     * connection with the given name, <code>NULL</code> will be returned instead.
     *
     * @param connection The name of the connection to fetch.
     *
     * @return either (1) the database connection instance with the given name
     *         or (2) <code>NULL</code> if the no database instance exists with the given name
     */
    public Database getConnection(String connection)
    {
        connection = connection.toLowerCase();

        if (connections.containsKey(connection)) {
            return connections.get(connection).getConnection();
        }

        return null;
    }

    /**
     * Returns the default database connection, if there are not default connection the
     * database with the highest database connection level will be returned instead.
     *
     * @return either (1) The default or highest level database connection
     *         or (2) <code>NULL</code> if there are no connections in the container.
     */
    public Database getDefaultConnection()
    {
        if (defaultConnection != null) {
            return connections.get(defaultConnection).getConnection();
        }

        Connection connection = null;

        for (String name : connections.keySet()) {
            Connection db = connections.get(name);

            if (connection == null) {
                connection = db;
                continue;
            }

            if (db.getLevel().getLevel() > connection.getLevel().getLevel()) {
                connection = db;
            }
        }

        if (connection == null) {
            return null;
        }

        return connection.getConnection();
    }

    /**
     * Returns a map of database connection instances.
     *
     * @return a map of database connection instances.
     */
    public Map<String, Connection> getConnections()
    {
        return connections;
    }
}
