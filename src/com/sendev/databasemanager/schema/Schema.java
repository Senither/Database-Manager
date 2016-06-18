package com.sendev.databasemanager.schema;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.Database;
import com.sendev.databasemanager.grammar.CreateGrammar;
import com.sendev.databasemanager.query.QueryType;
import com.sendev.databasemanager.schema.contracts.DatabaseClosure;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Schema
{
    private final DatabaseManager dbm;

    public Schema(DatabaseManager dbm)
    {
        this.dbm = dbm;
    }

    public boolean hasTable(String table) throws SQLException
    {
        dbm.output().debug("Schema::hasTable was called on \"%s\"", table);

        return dbm.getConnections().getDefaultConnection().hasTable(table);
    }

    public boolean hasColumn(String table, String column) throws SQLException
    {
        DatabaseMetaData meta = getMetaData();

        dbm.output().debug("Schema::hasColumn was called on \"%s.%s\"", table, column);

        ResultSet result = meta.getColumns(null, null, table, column);

        return result.next();
    }

    public boolean create(String table, DatabaseClosure closure) throws SQLException
    {
        dbm.output().debug("Schema::create was called on \"%s\"! Calling blueprint to generate the query...", table);

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        CreateGrammar grammar = createGrammar(true);

        String query = grammar.format(blueprint);

        dbm.output().debug("create query was generated, executing query: %s", query);

        return !dbm.getConnections().getDefaultConnection().prepare(query).execute();
    }

    public boolean create(String table, DatabaseClosure closure, boolean ignoreDatabasePrefix) throws SQLException
    {
        dbm.output().debug("Schema::create was called on \"%s\"! Calling blueprint to generate the query...", table);

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        CreateGrammar grammar = createGrammar(true, ignoreDatabasePrefix);

        String query = grammar.format(blueprint);

        dbm.output().debug("create query was generated, executing query: %s", query);

        return !dbm.getConnections().getDefaultConnection().prepare(query).execute();
    }

    public boolean createIfNotExists(String table, DatabaseClosure closure) throws SQLException
    {
        dbm.output().debug("Schema::createIfNotExists was called on \"%s\"! Calling blueprint to generate the query...", table);

        if (dbm.getConnections().getDefaultConnection().hasTable(prefixTable(table))) {
            dbm.output().debug("Schema::createIfNotExists table \"%s\" was found, returning false", table);
            return false;
        }

        Blueprint blueprint = createAndRunBlueprint(table, closure);

        CreateGrammar grammar = createGrammar(false);

        String query = grammar.format(blueprint);

        dbm.output().debug("Schema::createIfNotExists query was generated, executing query: %s", query);

        return !dbm.getConnections().getDefaultConnection().prepare(query).execute();
    }

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

        CreateGrammar grammar = createGrammar(false, ignoreDatabasePrefix);

        String query = grammar.format(blueprint);

        dbm.output().debug("Schema::createIfNotExists query was generated, executing query: %s", query);

        return !dbm.getConnections().getDefaultConnection().prepare(query).execute();
    }

    private Blueprint createAndRunBlueprint(String table, DatabaseClosure closure)
    {
        Blueprint blueprint = new Blueprint(table);

        closure.run(blueprint);

        return blueprint;
    }

    private CreateGrammar createGrammar(boolean shouldIgnoreExistingTable)
    {
        return createGrammar(shouldIgnoreExistingTable, false);
    }

    private CreateGrammar createGrammar(boolean shouldIgnoreExistingTable, boolean shouldIgnoreDatabasePrefix)
    {
        try {
            CreateGrammar grammar = (CreateGrammar) QueryType.CREATE.getGrammar().newInstance();

            grammar.ignoreExistingTable(shouldIgnoreExistingTable);
            grammar.ignoreDatabasePrefix(shouldIgnoreDatabasePrefix);

            return grammar;
        } catch (InstantiationException ex) {
            dbm.output().exception("Invalid grammar object parsed, failed to create a new instance!", ex);
        } catch (IllegalAccessException ex) {
            dbm.output().exception("An attempt was made to create a grammar instance on an object that is not accessible!", ex);
        }

        return null;
    }

    public boolean drop(String table) throws SQLException
    {
        table = prefixTable(table);

        dbm.output().debug("Schema::drop was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE `%s`;", table));
    }

    public boolean drop(String table, boolean ignoreDatabasePrefix) throws SQLException
    {
        if (!ignoreDatabasePrefix) {
            return drop(table);
        }

        dbm.output().debug("Schema::drop was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE `%s`;", table));
    }

    public boolean dropIfExists(String table) throws SQLException
    {
        table = prefixTable(table);

        dbm.output().debug("Schema::dropIfExists was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE IF EXISTS `%s`;", table));
    }

    public boolean dropIfExists(String table, boolean ignoreDatabasePrefix) throws SQLException
    {
        if (!ignoreDatabasePrefix) {
            return dropIfExists(table);
        }

        dbm.output().debug("Schema::dropIfExists was called on \"%s\"!", table);

        return alterQuery(format("DROP TABLE IF EXISTS `%s`;", table));
    }

    public boolean rename(String from, String to) throws SQLException
    {
        dbm.output().debug("Schema::rename was called on \"%s\", renaming to \"%s\"", from, to);

        return alterQuery(format("ALTER TABLE `%s` RENAME `%s`;", from, to));
    }

    public boolean renameIfExists(String from, String to) throws SQLException
    {
        dbm.output().debug("Schema::renameIfExists was called on \"%s\", checking if the table exists...", from);

        if (!hasTable(from)) {
            return false;
        }

        return rename(from, to);
    }

    private boolean alterQuery(String query) throws SQLException
    {
        dbm.output().debug("DatabaseManager::alterQuery was called with the following SQL statement: %s", query);

        Statement stmt = getDefaultConnection().getConnection().createStatement();

        return stmt.execute(query);
    }

    private String format(String query, Object... items)
    {
        return String.format(query, items);
    }

    private DatabaseMetaData getMetaData() throws SQLException
    {
        return getDefaultConnection().getConnection().getMetaData();
    }

    private Database getDefaultConnection()
    {
        return dbm.getConnections().getDefaultConnection();
    }

    private String prefixTable(String table)
    {
        String prefix = dbm.options().getPrefix();

        if (prefix.length() > 0) {
            table = prefix + table;
        }

        return table;
    }
}
