package com.sendev.databasemanager.contracts;

import java.sql.SQLException;

public interface DatabaseContract
{

    /**
     * Attempts to open the database connection to the database
     * type, this will return true if it manages to connect
     * to the database, and false otherwise.
     *
     * @return Boolean
     */
    public abstract boolean open();

    /**
     * Attempts to get the database statement from the query.
     *
     * @param query The query to check.
     * @return StatementContract
     * @throws SQLException
     */
    public abstract StatementContract getStatement(String query) throws SQLException;

    /**
     * Attempts to find out if the parsed string is a table.
     *
     * @param table The table name to check.
     * @return Boolean
     */
    public abstract boolean isTable(String table);

    /**
     * Attempts to truncate the given table, this will delete
     * every record in the table and reset it completely.
     *
     * @param table The table name to truncate.
     * @return Boolean
     */
    public abstract boolean truncate(String table);
}
