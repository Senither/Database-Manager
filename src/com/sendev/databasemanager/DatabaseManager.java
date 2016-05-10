package com.sendev.databasemanager;

import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.contracts.DatabaseOutput;
import com.sendev.databasemanager.exceptions.DatabaseException;
import com.sendev.databasemanager.output.OutputMode;
import com.sendev.databasemanager.query.QueryBuilder;
import com.sendev.databasemanager.schema.Schema;
import com.sendev.databasemanager.utils.Collection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import org.bukkit.plugin.Plugin;

public final class DatabaseManager
{
    private final Plugin plugin;
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
     * @see com.sendev.databasemanager.output.OutputMode Database OutputMode, the DBM data logger.
     *
     * @param plugin The Bukkit plugin instance for the plugin using the Database Manager.
     */
    public DatabaseManager(Plugin plugin)
    {
        this.plugin = plugin;

        setOutputMode(OutputMode.SILENT);
    }

    public DatabaseOptions options()
    {
        return options;
    }

    public Schema schema()
    {
        return new Schema(this);
    }

    public Plugin plugin()
    {
        return plugin;
    }

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

    public DatabaseOutput output()
    {
        return output;
    }

    /**
     * Adds a database connection to the connections container, allowing
     * queries to be run against the connection.
     * <p>
     * The <code>connection level</code> will be used to determin the priority of the connection,
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
     * @param connection the
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

    public Collection query(QueryBuilder query) throws SQLException
    {
        return query(query.toSQL());
    }

    /**
     * Executes the given SQL statement, which returns a single
     * <code>Collection</code> object.
     *
     * @param connection a valid connection name, the connection name must be in the connections container.
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
    public Collection query(String connection, QueryBuilder query) throws SQLException
    {
        return query(connection, query.toSQL());
    }

    public int queryUpdate(String query) throws SQLException
    {
        output.debug("DatabaseManager::queryUpdate was called with the following SQL statement: %s", query);

        PreparedStatement stmt = connections.getDefaultConnection().prepare(query);

        return stmt.executeUpdate();
    }

    public int queryUpdate(QueryBuilder query) throws SQLException
    {
        return queryUpdate(query.toSQL());
    }
}
