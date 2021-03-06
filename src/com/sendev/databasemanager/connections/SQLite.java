package com.sendev.databasemanager.connections;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

import com.sendev.databasemanager.contracts.FilenameDatabase;
import com.sendev.databasemanager.contracts.StatementContract;
import com.sendev.databasemanager.exceptions.DatabaseException;

public class SQLite extends FilenameDatabase
{

    /**
     * Creates a new SQLite database connection,
     * this will create an in-memory database.
     */
    public SQLite()
    {
    }

    public SQLite(String directory, String database)
    {
        super(directory, database.split("\\.")[0], database.split("\\.")[1]);
    }

    /**
     * Creates a new SQLite database connection.
     *
     * @param directory The folder the database is located in.
     * @param filename  The name of the database file.
     * @param extension The extension of the database file.
     */
    public SQLite(String directory, String filename, String extension)
    {
        super(directory, filename, extension);
    }

    @Override
    protected boolean initialize()
    {
        try {
            Class.forName("org.sqlite.JDBC");

            return true;
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Class not found while initializing", e);
        }
    }

    @Override
    public boolean open() throws SQLException
    {
        if (initialize()) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + (getFile() == null ? ":memory:" : getFile().getAbsolutePath()));

                return true;
            } catch (SQLException e) {
                String reason = "DBM - Could not establish an SQLite connection, SQLException: " + e.getMessage();

                dbm.output().exception(reason, e);
                throw new SQLException(reason);
            }
        }

        return false;
    }

    @Override
    protected void queryValidation(StatementContract paramStatement) throws SQLException
    {
        // This does nothing for SQLite
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        open();

        return connection;
    }

    @Override
    public StatementContract getStatement(String query) throws SQLException
    {
        String[] statement = query.trim().split(" ", 2);

        try {
            return SQLiteStatement.valueOf(statement[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SQLException(String.format("Unknown statement: \"%s\".", statement[0]));
        }
    }

    @Override
    public boolean hasTable(String table)
    {
        try {
            DatabaseMetaData md = getConnection().getMetaData();

            try (ResultSet tables = md.getTables(null, null, table, null)) {
                if (tables.next()) {
                    tables.close();

                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "DBM - Could not check if table \"{0}\" exists, SQLException: {1}", new Object[]{table, e.getMessage()});
        }

        return false;
    }

    @Override
    public boolean truncate(String table)
    {
        try {
            if (!hasTable(table)) {
                getLogger().log(Level.SEVERE, "DBM - Table \"{0}\" does not exist.", table);
                return false;
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException e) {
            if ((!e.getMessage().toLowerCase().contains("locking")) && (!e.getMessage().toLowerCase().contains("locked")) && (!e.toString().contains("not return ResultSet"))) {
                getLogger().log(Level.SEVERE, "DBM - Error in truncate() query: {0}", e);
            }
        }

        return false;
    }

    @Override
    protected Statement createPreparedStatement(String query) throws SQLException
    {
        Statement statement = getConnection().createStatement();

        if (dbm != null) {
            if (dbm.options().getQueryTimeout() > 0) {
                statement.setQueryTimeout(dbm.options().getQueryTimeout());
            }

            if (dbm.options().getQueryReturnLimit() > 0) {
                statement.setMaxRows(dbm.options().getQueryReturnLimit());
            }
        }

        return statement;
    }
}
