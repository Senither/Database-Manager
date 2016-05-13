package com.sendev.databasemanager.connections;

import com.sendev.databasemanager.contracts.HostnameDatabase;
import com.sendev.databasemanager.contracts.StatementContract;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends HostnameDatabase
{
    /**
     * Creates a MySQL database connection instance with the parsed information, the port used will default to <code>3306</code>.
     *
     * @param hostname The hostname the database should connection to.
     * @param database The database name the database should connect to.
     * @param username The username to login to the database.
     * @param password The password to login to the database.
     */
    public MySQL(String hostname, String database, String username, String password)
    {
        super(hostname, 3306, database, username, password);
    }

    /**
     * Creates a MySQL database connection instance with the parsed information-
     *
     * @param hostname The hostname the database should connection to.
     * @param port     The port the database is listening on.
     * @param database The database name the database should connect to.
     * @param username The username to login to the database.
     * @param password The password to login to the database.
     */
    public MySQL(String hostname, int port, String database, String username, String password)
    {
        super(hostname, port, database, username, password);
    }

    @Override
    protected boolean initialize()
    {
        try {
            Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

            return true;
        } catch (ClassNotFoundException e) {
            dbm.output().exception("MySQL DataSource class missing: %s", e, e.getMessage());
        }

        return false;
    }

    @Override
    public boolean open()
    {
        try {
            String url = String.format("jdbc:mysql://%s:%d/%s", getHostname(), getPort(), getDatabase());

            if (initialize()) {
                connection = DriverManager.getConnection(url, getUsername(), getPassword());

                return true;
            }

        } catch (SQLException e) {
            dbm.output().exception("Could not establish a MySQL connection, SQLException: %s", e, e.getMessage());
        }

        return false;
    }

    @Override
    protected void queryValidation(StatementContract statement) throws SQLException
    {
        SQLException exception;

        switch ((MySQLStatement) statement) {
            case USE:
                exception = new SQLException("Please create a new connection to use a different database.");

                dbm.output().exception("Please create a new connection to use a different database.", exception);

                throw exception;

            case PREPARE:
            case EXECUTE:
            case DEALLOCATE:
                exception = new SQLException("Please use the prepare() method to prepare a query.");

                dbm.output().exception("Please use the prepare() method to prepare a query.", exception);

                throw exception;
        }
    }

    @Override
    public StatementContract getStatement(String query) throws SQLException
    {
        String[] statement = query.trim().split(" ", 2);

        try {
            return MySQLStatement.valueOf(statement[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            dbm.output().exception("Unknown statement: \"%s\"", e, statement[0]);
        }

        return null;
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

            return true;
        } catch (SQLException e) {
            dbm.output().exception("Failed to check if table exists \"%s\": %s", e, table, e.getMessage());
        }

        return false;
    }

    @Override
    public boolean truncate(String table)
    {
        try {
            if (!hasTable(table)) {
                return false;
            }

            try (Statement statement = getConnection().createStatement()) {
                statement.executeUpdate(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException e) {
            dbm.output().exception("Failed to truncate \"%s\": %s", e, table, e.getMessage());
        }

        return false;
    }
}
