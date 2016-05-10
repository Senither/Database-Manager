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

    public boolean addConnection(String name, ConnectionLevel level, Database connection)
    {
        name = name.toLowerCase();

        if (connections.containsKey(name)) {
            return false;
        }

        connections.put(name, new Connection(dbm, level, connection));

        return true;
    }

    public Database getConnection(String connection)
    {
        connection = connection.toLowerCase();

        if (connections.containsKey(connection)) {
            return connections.get(connection).getConnection();
        }

        return null;
    }

    public Database getDefaultConnection()
    {
        if (defaultConnection != null) {
            return connections.get(defaultConnection).getConnection();
        }

        Connection connection = null;

        for (String name : connections.keySet()) {
            Connection db = connections.get(name);

            if (db.getLevel().equals(ConnectionLevel.DEFAULT)) {
                defaultConnection = name;
                return db.getConnection();
            }

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
}
