package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.query.QueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

public abstract class Database implements DatabaseContract
{

    protected DatabaseManager dbm = null;

    /**
     * Represents our prepared query statements and their statement
     * type, allowing us to quickly render and compile statements.
     */
    protected Map<PreparedStatement, StatementContract> preparedStatements = new HashMap();
    ;

    /**
     * Represents our current database connection, this is
     * used to send queries to the database, as well as
     * fetch, and persist data.
     */
    protected Connection connection;

    /**
     * Represents a unix timestamp of the last time we
     * communicated with the database.
     */
    protected int lastUpdate;

    public void setDatabaseManager(DatabaseManager dbm)
    {
        this.dbm = dbm;
    }

    /**
     * Initialize the database abstraction, this should be
     * called by the open method if it's necessary.
     *
     * @return Boolean
     */
    protected abstract boolean initialize();

    /**
     * Checks a statement for faults, issues, overlaps,
     * deprecated calls and other issues.
     *
     * @param paramStatement The statement to check.
     *
     * @throws SQLException
     */
    protected abstract void queryValidation(StatementContract paramStatement) throws SQLException;

    /**
     * Closes the database connection.
     *
     * @return Boolean
     *
     * @throws java.sql.SQLException
     */
    public final boolean close() throws SQLException
    {
        if (connection == null) {
            getLogger().warning("DBM:Database - Could not close connection, it is null.");
            return false;
        }

        try {
            connection.close();

            return true;
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "DBM:Database - Could not close connection, SQLException: {0}", e.getMessage());
        }
        return false;
    }

    /**
     * Returns the current database connection, if the
     * connection is not open/active, it will attempt
     * to open the connection for you.
     *
     * @return Connection
     * @throws java.sql.SQLException
     */
    public final Connection getConnection() throws SQLException
    {
        if (!isOpen()) {
            open();
        }

        return connection;
    }

    /**
     * Checks to see if the database connection is still valid.
     *
     * @return Boolean
     */
    public final boolean isOpen() 
    {
        return isOpen(1);
    }

    /**
     * Checks to see if the database connection is still valid.
     *
     * @param seconds The amount of time to wait for the connection for.
     *
     * @return Boolean
     */
    public final boolean isOpen(int seconds)
    {
        if (connection != null) {
            try {
                if (connection.isValid(seconds)) {
                    return true;
                }
            } catch (SQLException e) {
            }
        }

        return false;
    }

    /**
     * Get the unix timestamp of the last time the class
     * communicated with the database.
     *
     * @return Integer
     */
    public final int getLastUpdateCount()
    {
        return lastUpdate;
    }

    /**
     * Queries the database with the given query.
     *
     * @param query The query to run.
     *
     * @return ResultSet
     *
     * @throws SQLException
     */
    public final synchronized ResultSet query(String query) throws SQLException
    {
        queryValidation(getStatement(query));

        PreparedStatement statement = createPreparedStatement(query);

        if (statement.execute(query)) {
            return statement.getResultSet();
        }

        return getConnection().createStatement().executeQuery("SELECT " + (lastUpdate = statement.getUpdateCount()));
    }

    /**
     * Queries the database with the query built from the query builder object.
     *
     * @param builder The query to build.
     *
     * @return ResultSet
     *
     * @throws SQLException
     */
    public final synchronized ResultSet query(QueryBuilder builder) throws SQLException
    {
        return query(builder.toSQL());
    }

    /**
     * Queries the database with the given prepared statement.
     *
     * @param query     The prepared statement to run.
     * @param statement The query statement.
     *
     * @return ResultSet
     *
     * @throws SQLException
     */
    public final synchronized ResultSet query(PreparedStatement query, StatementContract statement) throws SQLException
    {
        queryValidation(statement);

        if (query.execute()) {
            return query.getResultSet();
        }

        return getConnection().createStatement().executeQuery("SELECT " + (lastUpdate = query.getUpdateCount()));
    }

    /**
     * Queries the database with the given prepared statement.
     *
     * @param query The prepared statement to run.
     *
     * @return ResultSet
     *
     * @throws SQLException
     */
    public final synchronized ResultSet query(PreparedStatement query) throws SQLException
    {
        ResultSet output = query(query, (StatementContract) preparedStatements.get(query));

        preparedStatements.remove(query);

        return output;
    }

    /**
     * Prepares a query as a prepared statement before executing it.
     *
     * @param query The query to prepare.
     *
     * @return PreparedStatement
     *
     * @throws SQLException
     */
    public final synchronized PreparedStatement prepare(String query) throws SQLException
    {
        StatementContract statement = getStatement(query);
        PreparedStatement ps = createPreparedStatement(query);

        preparedStatements.put(ps, statement);

        return ps;
    }

    /**
     * Stores data in the database from the given query, this
     * will return a list of ids from the inserted rows.
     *
     * @param query The query to run.
     *
     * @return ArrayList
     *
     * @throws SQLException
     */
    public final synchronized ArrayList<Long> insert(String query) throws SQLException
    {
        ArrayList<Long> keys = new ArrayList();

        PreparedStatement pstmt = createPreparedStatement(query, 1);
        lastUpdate = pstmt.executeUpdate();

        ResultSet key = pstmt.getGeneratedKeys();
        if (key.next()) {
            keys.add(key.getLong(1));
        }

        return keys;
    }

    /**
     * Stores data in the database from the given prepared statement,
     * this will return a list of ids from the inserted rows.
     *
     * @param query The prepared statement to run.
     *
     * @return ArrayList
     *
     * @throws SQLException
     */
    public final synchronized ArrayList<Long> insert(PreparedStatement query) throws SQLException
    {
        lastUpdate = query.executeUpdate();
        preparedStatements.remove(query);

        ArrayList<Long> keys = new ArrayList();
        ResultSet key = query.getGeneratedKeys();
        if (key.next()) {
            keys.add(key.getLong(1));
        }

        return keys;
    }

    private PreparedStatement createPreparedStatement(String query) throws SQLException
    {
        PreparedStatement statement = getConnection().prepareStatement(query);

        if (dbm.options().getQueryTimeout() > 0) {
            statement.setQueryTimeout(dbm.options().getQueryTimeout());
        }

        if (dbm.options().getQueryReturnLimit() > 0) {
            statement.setMaxRows(dbm.options().getQueryReturnLimit());
        }

        return statement;
    }

    private PreparedStatement createPreparedStatement(String query, int autoGeneratedKeys) throws SQLException
    {
        PreparedStatement statement = getConnection().prepareStatement(query, autoGeneratedKeys);

        if (dbm.options().getQueryTimeout() > 0) {
            statement.setQueryTimeout(dbm.options().getQueryTimeout());
        }

        if (dbm.options().getQueryReturnLimit() > 0) {
            statement.setMaxRows(dbm.options().getQueryReturnLimit());
        }

        return statement;
    }
}
