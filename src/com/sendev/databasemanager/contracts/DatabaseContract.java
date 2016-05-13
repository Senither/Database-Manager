package com.sendev.databasemanager.contracts;

import java.sql.SQLException;

public interface DatabaseContract
{

    /**
     * Attempts to open the database connection to the database
     * type, this will return true if it manages to connect
     * to the database, and false otherwise.
     *
     * @return true if the database connection is open, false otherwise.
     */
    public abstract boolean open();

    /**
     * Attempts to get the database statement from the query.
     *
     * @param query The query to check.
     *
     * @return The implementation of the statement contract.
     *
     * @exception SQLException if a database access error occurs,
     *                         this method is called on a closed <code>Statement</code>, the given
     *                         SQL statement produces anything other than a single
     *                         <code>ResultSet</code> object, the method is called on a
     *                         <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public abstract StatementContract getStatement(String query) throws SQLException;

    /**
     * Attempts to find out if the parsed string is a table.
     *
     * @param table The table name to check.
     *
     * @return true if the table exists, false otherwise.
     */
    public abstract boolean hasTable(String table);

    /**
     * Attempts to truncate the given table, this will delete
     * every record in the table and reset it completely.
     *
     * @param table The table name to truncate.
     *
     * @return true if the table was successfully reset, false otherwise.
     */
    public abstract boolean truncate(String table);
}
