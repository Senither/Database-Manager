package com.sendev.databasemanager.schema;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.exceptions.DatabaseException;
import com.sendev.databasemanager.grammar.CreateParser;
import com.sendev.databasemanager.query.QueryType;
import com.sendev.databasemanager.schema.contracts.DatabaseClosure;

public class Schema
{
    /**
     * The DBM main instance.
     */
    private final DatabaseManager dbm;

    /**
     * Creates a new Schematic instance for the provided DBM instance.
     *
     * @param dbm The DMM instance the schema instance should be created for
     */
    public Schema(DatabaseManager dbm)
    {
        this.dbm = dbm;
    }

    /**
     * Checks if the default connection has the provided table name.
     *
     * @param table The table to check if exists
     *
     * @return <code>TRUE</code> if the table exists, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean hasTable(String table) throws SQLException
    {
        dbm.output().debug("Schema::hasTable was called on \"%s\"", table);

        return dbm.getConnections().getDefaultConnection().hasTable(table);
    }

    /**
     * Checks if the provided connection has the provided table name.
     *
     * @param connection The database that should be checked
     * @param table      The table to check if exists
     *
     * @return <code>TRUE</code> if the table exists, <code>FALSE</code> otherwise.
     *
     * @throws SQLException      if a database access error occurs,
     *                           this method is called on a closed <code>Statement</code>, the given
     *                           SQL statement produces anything other than a single
     *                           <code>ResultSet</code> object, the method is called on a
     *                           <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws DatabaseException if the connection name is invalid or isn't registered in the connections container.
     */
    public boolean hasTable(String connection, String table) throws SQLException
    {
        dbm.output().debug("Schema::hasTable was called on \"%s\"", table);

        Database conn = dbm.getConnections().getConnection(connection);

        if (conn == null) {
            throw new DatabaseException("Invalid connection, there are no connection with the name \"" + connection + "\"");
        }

        return conn.hasTable(table);
    }

    /**
     * Checks if the default connection has the provided column for the given table.
     *
     * @param table  The table to use
     * @param column The column to check if exists
     *
     * @return <code>TRUE</code> if the column exists, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean hasColumn(String table, String column) throws SQLException
    {
        DatabaseMetaData meta = getMetaData();

        dbm.output().debug("Schema::hasColumn was called on \"%s.%s\"", table, column);

        ResultSet result = meta.getColumns(null, null, table, column);

        return result.next();
    }

    /**
     * Checks if the provided connection has the provided column for the given table.
     *
     * @param connection The database that should be checked
     * @param table      The table to use
     * @param column     The column to check if exists
     *
     * @return <code>TRUE</code> if the column exists, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean hasColumn(String connection, String table, String column) throws SQLException
    {
        DatabaseMetaData meta = getMetaData();

        dbm.output().debug("Schema::hasColumn was called on \"%s.%s\"", table, column);

        ResultSet result = meta.getColumns(null, null, table, column);

        return result.next();
    }

    /**
     * Creates a new table using the {@link DatabaseClosure} and {@link Blueprint} classes.
     *
     * @param table   The table that should be created
     * @param closure The database closure that creates the blueprint
     *
     * @return <code>TRUE</code> if the table was created successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean create(String table, DatabaseClosure closure) throws SQLException
    {
        dbm.output().debug("Schema::create was called on \"%s\"! Calling blueprint to generate the query...", table);

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        DatabaseManager dynamicDBM = DatabaseFactory.getDynamicOrigin(blueprint.getClass());

        CreateParser grammar = createGrammar(true);

        String query = grammar.parse(dynamicDBM, dynamicDBM.getConnections().getDefaultConnection(), blueprint);

        dbm.output().debug("create query was generated, executing query: %s", query);

        Statement stmt = dbm.getConnections().getDefaultConnection().prepare(query);

        if (stmt instanceof PreparedStatement) {
            return !((PreparedStatement) stmt).execute();
        }

        return !stmt.execute(query);
    }

    /**
     * Creates a new table using the {@link DatabaseClosure} and {@link Blueprint} classes.
     *
     * @param table                The table that should be created
     * @param closure              The database closure that creates the blueprint
     * @param ignoreDatabasePrefix Determines if the table should ignore the database prefix or not
     *
     * @return <code>TRUE</code> if the table was created successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean create(String table, DatabaseClosure closure, boolean ignoreDatabasePrefix) throws SQLException
    {
        dbm.output().debug("Schema::create was called on \"%s\"! Calling blueprint to generate the query...", table);

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        DatabaseManager dynamicDBM = DatabaseFactory.getDynamicOrigin(blueprint.getClass());

        CreateParser grammar = createGrammar(true, ignoreDatabasePrefix);

        String query = grammar.parse(dynamicDBM, dynamicDBM.getConnections().getDefaultConnection(), blueprint);

        dbm.output().debug("create query was generated, executing query: %s", query);

        Statement stmt = dbm.getConnections().getDefaultConnection().prepare(query);

        if (stmt instanceof PreparedStatement) {
            return !((PreparedStatement) stmt).execute();
        }

        return !stmt.execute(query);
    }

    /**
     * Creates a new table if it doesn't exists using the {@link DatabaseClosure} and {@link Blueprint} classes.
     *
     * @param table   The table that should be created
     * @param closure The database closure that creates the blueprint
     *
     * @return <code>TRUE</code> if the table was created successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean createIfNotExists(String table, DatabaseClosure closure) throws SQLException
    {
        dbm.output().debug("Schema::createIfNotExists was called on \"%s\"! Calling blueprint to generate the query...", table);

        if (dbm.getConnections().getDefaultConnection().hasTable(prefixTable(table))) {
            dbm.output().debug("Schema::createIfNotExists table \"%s\" was found, returning false", table);
            return false;
        }

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        DatabaseManager dynamicDBM = DatabaseFactory.getDynamicOrigin(blueprint.getClass());

        CreateParser grammar = createGrammar(false);

        String query = grammar.parse(dynamicDBM, dynamicDBM.getConnections().getDefaultConnection(), blueprint);

        dbm.output().debug("Schema::createIfNotExists query was generated, executing query: %s", query);

        Statement stmt = dbm.getConnections().getDefaultConnection().prepare(query);

        if (stmt instanceof PreparedStatement) {
            return !((PreparedStatement) stmt).execute();
        }

        return !stmt.execute(query);
    }

    /**
     * Creates a new table if it doesn't exists using the {@link DatabaseClosure} and {@link Blueprint} classes.
     *
     * @param table                The table that should be created
     * @param closure              The database closure that creates the blueprint
     * @param ignoreDatabasePrefix Determines if the table should ignore the database prefix or not
     *
     * @return <code>TRUE</code> if the table was created successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean createIfNotExists(String table, DatabaseClosure closure, boolean ignoreDatabasePrefix) throws SQLException
    {
        if (!ignoreDatabasePrefix) {
            return createIfNotExists(table, closure);
        }

        dbm.output().debug("Schema::createIfNotExists was called on \"%s\"! Calling blueprint to generate the query...", table);

        if (dbm.getConnections().getDefaultConnection().hasTable(table)) {
            dbm.output().debug("Schema::createIfNotExists table \"%s\" was found, returning false", table);
            return false;
        }

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        DatabaseManager dynamicDBM = DatabaseFactory.getDynamicOrigin(blueprint.getClass());

        CreateParser grammar = createGrammar(false, ignoreDatabasePrefix);

        String query = grammar.parse(dynamicDBM, dynamicDBM.getConnections().getDefaultConnection(), blueprint);

        dbm.output().debug("Schema::createIfNotExists query was generated, executing query: %s", query);

        Statement stmt = dbm.getConnections().getDefaultConnection().prepare(query);

        if (stmt instanceof PreparedStatement) {
            return !((PreparedStatement) stmt).execute();
        }

        return !stmt.execute(query);
    }

    /**
     * Creates and runs a blueprint for the provided closure for the given table.
     *
     * @param table   The table the blueprint should be created for
     * @param closure The closure that should run the blueprint
     *
     * @return The blueprint that was created.
     */
    private Blueprint createAndRunBlueprint(String table, DatabaseClosure closure)
    {
        Blueprint blueprint = new Blueprint(table);

        closure.run(blueprint);

        return blueprint;
    }

    /**
     * Creates a {@link QueryType#CREATE} grammar instance that doesn't ignores the database prefix.
     *
     * @param shouldIgnoreExistingTable Determines if the grammar instance should ignore existing tables
     *
     * @return The {@link QueryType#CREATE} grammar instance.
     */
    private CreateParser createGrammar(boolean shouldIgnoreExistingTable)
    {
        return createGrammar(shouldIgnoreExistingTable, false);
    }

    /**
     * Creates a {@link QueryType#CREATE} grammar instance with the provided settings.
     *
     * @param shouldIgnoreExistingTable  Determines if the grammar instance should ignore existing tables
     * @param shouldIgnoreDatabasePrefix Determines if the grammar instance should ignore the database prefix
     *
     * @return The {@link QueryType#CREATE} grammar instance.
     */
    private CreateParser createGrammar(boolean shouldIgnoreExistingTable, boolean shouldIgnoreDatabasePrefix)
    {
        try {
            CreateParser grammar = (CreateParser) QueryType.CREATE.getGrammar().newInstance();

            grammar.setOption("ignoreExistingTable", shouldIgnoreExistingTable);
            grammar.setOption("ignoreDatabasePrefix", shouldIgnoreDatabasePrefix);

            return grammar;
        } catch (InstantiationException ex) {
            dbm.output().exception("Invalid grammar object parsed, failed to create a new instance!", ex);
        } catch (IllegalAccessException ex) {
            dbm.output().exception("An attempt was made to create a grammar instance on an object that is not accessible!", ex);
        }

        return null;
    }

    /**
     * Drops the provided table, if the table doesn't exist an exception will be thrown.
     *
     * @param table The table that should be dropped
     *
     * @return <code>TRUE</code> if the table was dropped successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean drop(String table) throws SQLException
    {
        table = prefixTable(table);

        dbm.output().debug("Schema::drop was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE `%s`;", table));
    }

    /**
     * Drops the provided table, if the table doesn't exist an exception will be thrown.
     *
     * @param table                The table that should be dropped
     * @param ignoreDatabasePrefix Determines if the table should ignore the database prefix
     *
     * @return <code>TRUE</code> if the table was dropped successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean drop(String table, boolean ignoreDatabasePrefix) throws SQLException
    {
        if (!ignoreDatabasePrefix) {
            return drop(table);
        }

        dbm.output().debug("Schema::drop was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE `%s`;", table));
    }

    /**
     * Drops the provided table if it exists.
     *
     * @param table The table that should be dropped
     *
     * @return <code>TRUE</code> if the table was dropped successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean dropIfExists(String table) throws SQLException
    {
        table = prefixTable(table);

        dbm.output().debug("Schema::dropIfExists was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE IF EXISTS `%s`;", table));
    }

    /**
     * Drops the provided table if it exists, if it doesn't exist nothing will happen.
     *
     * @param table                The table that should be dropped
     * @param ignoreDatabasePrefix Determines if the table should ignore the database prefix
     *
     * @return <code>TRUE</code> if the table was dropped successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean dropIfExists(String table, boolean ignoreDatabasePrefix) throws SQLException
    {
        if (!ignoreDatabasePrefix) {
            return dropIfExists(table);
        }

        dbm.output().debug("Schema::dropIfExists was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE IF EXISTS `%s`;", table));
    }

    /**
     * Renames the provided table to the new name using the database prefix, if
     * the table doesn't exist an exception will be thrown.
     * <p>
     * <Strong>Note:</strong> Both names are affected by the database prefix.
     *
     * @param from The tables current name
     * @param to   The name the table should be renamed to
     *
     * @return <code>TRUE</code> if the table was renamed successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean rename(String from, String to) throws SQLException
    {
        return rename(from, to, false);
    }

    /**
     * Renames the provided table to the new name, if the table doesn't exist an exception will be thrown.
     * <p>
     * <Strong>Note:</strong> If <code>FALSE</code> is parsed to the ignoreDatabasePrefix
     * parameter, both names are affected by the database prefix.
     *
     *
     * @param from                 The tables current name
     * @param to                   The name the table should be renamed to
     * @param ignoreDatabasePrefix Determines if the table should ignore the database prefix
     *
     * @return <code>TRUE</code> if the table was renamed successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean rename(String from, String to, boolean ignoreDatabasePrefix) throws SQLException
    {
        dbm.output().debug("Schema::rename was called on \"%s\", renaming to \"%s\"", from, to);

        if (!ignoreDatabasePrefix) {
            from = prefixTable(from);
            to = prefixTable(to);
        }

        return alterQuery(format("ALTER TABLE `%s` RENAME `%s`;", from, to));
    }

    /**
     * Renames the provided table to the new name if it exists, if the table doesn't exist nothing will happen.
     * <p>
     * <Strong>Note:</strong> If <code>FALSE</code> is parsed to the ignoreDatabasePrefix
     * parameter, both names are affected by the database prefix.
     *
     *
     * @param from The tables current name
     * @param to   The name the table should be renamed to
     *
     * @return <code>TRUE</code> if the table was renamed successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean renameIfExists(String from, String to) throws SQLException
    {
        dbm.output().debug("Schema::renameIfExists was called on \"%s\", checking if the table exists...", from);

        if (!hasTable(from)) {
            return false;
        }

        return rename(from, to);
    }

    /**
     * Creates an alter query statement for the default database connection, and then executes
     * the provided query using the {@link Statement#execute(java.lang.String) } method.
     *
     * @param query The query that should be executed
     *
     * @return <code>TRUE</code> if the query affected any rows/tables/columns successfully, <code>FALSE</code> otherwise.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    private boolean alterQuery(String query) throws SQLException
    {
        dbm.output().debug("DatabaseManager::alterQuery was called with the following SQL statement: %s", query);

        Statement stmt = getDefaultConnection().getConnection().createStatement();

        return !stmt.execute(query);
    }

    /**
     * Formats the provided string using the varargs items object array and
     * the {@link String#format(java.lang.String, java.lang.Object...) } method.
     *
     * @param query The string to format
     * @param items The varargs items to format the string with
     *
     * @return The formatted string.
     */
    private String format(String query, Object... items)
    {
        return String.format(query, items);
    }

    /**
     * Gets the database meta data object from the default database connection.
     *
     * @return The database meta data object.
     *
     * @throws SQLException
     */
    private DatabaseMetaData getMetaData() throws SQLException
    {
        return getDefaultConnection().getConnection().getMetaData();
    }

    /**
     * Gets the default database connection.
     *
     * @return The default database connection.
     */
    private Database getDefaultConnection()
    {
        return dbm.getConnections().getDefaultConnection();
    }

    /**
     * Prefix the provided string using the database prefix.
     *
     * @param table The string to prefix
     *
     * @return The prefixed string.
     */
    private String prefixTable(String table)
    {
        String prefix = dbm.options().getPrefix();

        if (prefix.length() > 0) {
            table = prefix + table;
        }

        return table;
    }
}
