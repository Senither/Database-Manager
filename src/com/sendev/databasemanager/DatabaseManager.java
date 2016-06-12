package com.sendev.databasemanager;

import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.contracts.DatabaseOutput;
import com.sendev.databasemanager.exceptions.DatabaseException;
import com.sendev.databasemanager.output.OutputMode;
import com.sendev.databasemanager.query.QueryBuilder;
import com.sendev.databasemanager.schema.Schema;
import com.sendev.databasemanager.utils.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.Plugin;

public final class DatabaseManager
{
    private final Plugin plugin;
    private final Schema schema = new Schema(this);
    private final DatabaseOptions options = new DatabaseOptions();
    private final ConnectionContainer connections = new ConnectionContainer(this);

    /**
     * The output represents our database logger output, depending on the
     * output mode currently set, the amount of messages will vary.
     */
    private DatabaseOutput output;

    /**
     * Creates a new database manager instance, this will sets up the database
     * options, container and set the output mode to <code>SILENT</code>.
     *
     * @see com.sendev.databasemanager.output.OutputMode
     *
     * @param plugin The Bukkit plugin instance for the plugin using the Database Manager.
     */
    public DatabaseManager(Plugin plugin)
    {
        this.plugin = plugin;

        setOutputMode(OutputMode.SILENT);
    }

    /**
     * Gets the database options/settings, allowing you to customize
     * how the Database Manager operates and talks to the database.
     *
     * @return The database options instance.
     */
    public DatabaseOptions options()
    {
        return options;
    }

    /**
     * Gets the database schema instance, allowing you to create new
     * tables, check if tables exists, rename or modify tables.
     * <p>
     * All the schema queries that are generated and executed are being
     * ran against the default or highest priority connection level.
     *
     * @see com.sendev.databasemanager.ConnectionLevel
     *
     * @return The database schema instance.
     */
    public Schema schema()
    {
        return schema;
    }

    /**
     * Returns the Bukkit JavaPlugin instance, this is used by some
     * of the internal operations in the Database Manager.
     *
     * @return The Bukkit JavaPlugin instance.
     */
    public Plugin plugin()
    {
        return plugin;
    }

    /**
     * This will change the Database Manager output mode, changing this
     * value will allow DBM to log more or less information to the
     * console, depending on the value you set it to.
     * <p>
     * <strong>Note:</strong> Debug messages has to be enabled from the <code>Database Options</code>
     *
     * @see com.sendev.databasemanager.output.OutputMode
     * @see com.sendev.databasemanager.DatabaseOptions
     *
     * @param mode The output mode you'd like to use.
     */
    public void setOutputMode(OutputMode mode)
    {
        try {
            Object instance = mode.getInstance().newInstance();

            if (instance instanceof DatabaseOutput) {
                output = (DatabaseOutput) instance;

                output.setDatabaseManager(this);
            }
        } catch (InstantiationException ex) {
            output().exception("Invalid grammar object parsed, failed to create a new instance!", ex);
        } catch (IllegalAccessException ex) {
            output().exception("An attempt was made to create a grammar instance on an object that is not accessible!", ex);
        }
    }

    /**
     * Returns the database output instance, allowing
     * you to log information to the console.
     *
     * @return The database output instance currently set by the DBM.
     */
    public DatabaseOutput output()
    {
        return output;
    }

    /**
     * Adds a database connection to the connections container, allowing
     * queries to be run against the connection.
     * <p>
     * The <code>connection level</code> will be used to determine the priority of the connection,
     * the top level will always be <code>DEFAULT</code>, after that the it's
     * <code>HIGEST</code>, <code>HIGE</code>, <code>MEDIUM</code>, etc...
     *
     * @param name     The name of the connection.
     * @param level    The level/priority of the connection.
     * @param database The database instance, this can be MySQL, SQLite, etc...
     *
     * @return <code>True</code> if the connection was added successfully, or
     *         <code>False</code> if the connection name already exists.
     */
    public boolean addConnection(String name, ConnectionLevel level, Database database)
    {
        output.debug("A new database connection is being added! Name: \"%s\", level: %s", name, level);

        return connections.addConnection(name, level, database);
    }

    /**
     * Returns the Database Manager connections container, this is where all your database
     * connections are stored and managed, you can use this instance to add database
     * connections directly, get a specific database connection, get the default
     * connection, disable/remove a connection and more.
     *
     * @return The Database Managers connection container.
     */
    public ConnectionContainer getConnections()
    {
        return connections;
    }

    /**
     * Executes the given SQL statement, which returns a single
     * <code>Collection</code> object.
     *
     * @param query an SQL statement to be sent to the database, typically a
     *              static SQL <code>SELECT</code> statement
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     *
     * @exception SQLException        if a database access error occurs,
     *                                this method is called on a closed <code>Statement</code>, the given
     *                                SQL statement produces anything other than a single
     *                                <code>ResultSet</code> object, the method is called on a
     *                                <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Collection query(String query) throws SQLException, SQLTimeoutException
    {
        output.debug("DatabaseManager::query was called with the following SQL statement: %s", query);

        return new Collection(connections.getDefaultConnection().query(query));
    }

    /**
     * Executes the given SQL statement, which returns a single
     * <code>Collection</code> object.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      an SQL statement to be sent to the database, typically a
     *                   static SQL <code>SELECT</code> statement
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     *
     * @exception SQLException        if a database access error occurs,
     *                                this method is called on a closed <code>Statement</code>, the given
     *                                SQL statement produces anything other than a single
     *                                <code>ResultSet</code> object, the method is called on a
     *                                <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Collection query(String connection, String query) throws SQLException, DatabaseException
    {
        output.debug("DatabaseManager::query was called on the connection \"%s\" with the following SQL statement: %s", connection, query);

        Database db = connections.getConnection(connection);

        if (db == null) {
            throw new DatabaseException("Invalid connection, there are no connection with the name \"" + connection + "\"");
        }

        return new Collection(db.query(query));
    }

    /**
     * Executes the SQL statement generated by the query builder, which returns a single
     * <code>Collection</code> object.
     *
     * @param query a QueryBuilder instance that should be sent to the database, typically a
     *              static SQL <code>SELECT</code> statement
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     *
     * @exception SQLException        if a database access error occurs,
     *                                this method is called on a closed <code>Statement</code>, the given
     *                                SQL statement produces anything other than a single
     *                                <code>ResultSet</code> object, the method is called on a
     *                                <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Collection query(QueryBuilder query) throws SQLException
    {
        return query(query.toSQL());
    }

    /**
     * Executes the SQL statement generated by the query builder, which returns a single
     * <code>Collection</code> object.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      a QueryBuilder instance that should be sent to the database, typically a
     *                   static SQL <code>SELECT</code> statement
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     *
     * @exception SQLException        if a database access error occurs,
     *                                this method is called on a closed <code>Statement</code>, the given
     *                                SQL statement produces anything other than a single
     *                                <code>ResultSet</code> object, the method is called on a
     *                                <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Collection query(String connection, QueryBuilder query) throws SQLException
    {
        return query(connection, query.toSQL());
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL Data
     * Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing, such as a DDL statement.
     *
     * @param query an SQL statement to be sent to the database, typically a static SQL DML statement
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public int queryUpdate(String query) throws SQLException
    {
        output.debug("DatabaseManager::queryUpdate was called with the following SQL statement: %s", query);

        PreparedStatement stmt = connections.getDefaultConnection().prepare(query);

        return stmt.executeUpdate();
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL Data
     * Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing, such as a DDL statement.
     *
     * @param query a QueryBuilder instance that should be sent to the database, typically a
     *              static SQL DML statement
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public int queryUpdate(QueryBuilder query) throws SQLException
    {
        return queryUpdate(query.toSQL());
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL Data
     * Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing, such as a DDL statement.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      an SQL statement to be sent to the database, typically a static SQL DML statement
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public int queryUpdate(String connection, String query) throws SQLException
    {
        output.debug("DatabaseManager::queryUpdate was called on the connection \"%s\" with the following SQL statement: %s", connection, query);

        Database db = connections.getConnection(connection);

        if (db == null) {
            throw new DatabaseException("Invalid connection, there are no connection with the name \"" + connection + "\"");
        }

        return db.prepare(query).executeUpdate();
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL Data
     * Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing, such as a DDL statement.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      a QueryBuilder instance that should be sent to the database, typically a
     *                   static SQL DML statement
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public int queryUpdate(String connection, QueryBuilder query) throws SQLException
    {
        return queryUpdate(connection, query.toSQL());
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL INSERT
     * statement, such as <code>INSERT</code>; After the query has been executed the prepared statement
     * will be used to generate a set of keys, referring to the IDs of the inserted rows.
     *
     * @param query an SQL statement to be sent to the database, typically a static SQL INSERT statement
     *
     * @return a set of IDs referring to the insert rows
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Set<Integer> queryInsert(String query) throws SQLException
    {
        output.debug("DatabaseManager::queryInsert was called with the following SQL statement: %s", query);

        if (!query.startsWith("INSERT INTO")) {
            throw new DatabaseException("queryInsert was called with a query without an INSERT statement!");
        }

        PreparedStatement stmt = connections.getDefaultConnection().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        stmt.executeUpdate();

        Set<Integer> ids = new HashSet<>();

        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            ids.add(keys.getInt(1));
        }

        return ids;
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL INSERT
     * statement, such as <code>INSERT</code>; After the query has been executed the prepared statement
     * will be used to generate a set of keys, referring to the IDs of the inserted rows.
     *
     * @param query a QueryBuilder instance that should be sent to the database, typically a
     *              static SQL INSERT statement
     *
     * @return a set of IDs referring to the insert rows
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Set<Integer> queryInsert(QueryBuilder query) throws SQLException
    {
        return queryInsert(query.toSQL());
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL INSERT
     * statement, such as <code>INSERT</code>; After the query has been executed the prepared statement
     * will be used to generate a set of keys, referring to the IDs of the inserted rows.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      an SQL statement to be sent to the database, typically a static SQL INSERT statement
     *
     * @return a set of IDs referring to the insert rows
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Set<Integer> queryInsert(String connection, String query) throws SQLException
    {
        output.debug("DatabaseManager::queryInsert was called on the connection \"%s\" with the following SQL statement: %s", connection, query);

        if (!query.startsWith("INSERT INTO")) {
            throw new DatabaseException("queryInsert was called with a query without an INSERT statement!");
        }

        Database db = connections.getConnection(connection);

        if (db == null) {
            throw new DatabaseException("Invalid connection, there are no connection with the name \"" + connection + "\"");
        }

        PreparedStatement stmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        stmt.executeUpdate();

        Set<Integer> ids = new HashSet<>();

        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            ids.add(keys.getInt(1));
        }

        return ids;
    }

    /**
     * Generates a prepared statement object and executes the SQL statement, which must be an SQL INSERT
     * statement, such as <code>INSERT</code>; After the query has been executed the prepared statement
     * will be used to generate a set of keys, referring to the IDs of the inserted rows.
     *
     * @param connection a specific database connection name that the query should be run against.
     * @param query      a QueryBuilder instance that should be sent to the database, typically a
     *                   static SQL INSERT statement
     *
     * @return a set of IDs referring to the insert rows
     *
     * @exception SQLException        if a database access error occurs;
     *                                this method is called on a closed  <code>PreparedStatement</code>
     *                                or the SQL statement returns a <code>ResultSet</code> object
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value that was specified by the {@code setQueryTimeout}
     *                             method has been exceeded and has at least attempted to cancel
     *                             the currently running {@code Statement}
     */
    public Set<Integer> queryInsert(String connection, QueryBuilder query) throws SQLException
    {
        return queryInsert(connection, query.toSQL());
    }
}
