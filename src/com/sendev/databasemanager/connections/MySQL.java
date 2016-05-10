package com.sendev.databasemanager.connections;

import com.sendev.databasemanager.contracts.HostnameDatabase;
import com.sendev.databasemanager.contracts.StatementContract;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

public class MySQL extends HostnameDatabase
{
    public MySQL(String hostname, String database, String username, String password)
    {
        super(hostname, 3306, database, username, password);
    }

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
            getLogger().log(Level.WARNING, "DBM - MySQL DataSource class missing: {0}", e.getMessage());
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
            getLogger().log(Level.SEVERE, "DBM - Could not establish a MySQL connection, SQLException: {0}", e.getMessage());
        }

        return false;
    }

    @Override
    protected void queryValidation(StatementContract statement) throws SQLException
    {
        switch ((MySQLStatement) statement) {
            case USE:
                getLogger().warning("DBM - Please create a new connection to use a different database.");
                throw new SQLException("Please create a new connection to use a different database.");

            case PREPARE:
            case EXECUTE:
            case DEALLOCATE:
                getLogger().warning("DBM - Please use the prepare() method to prepare a query.");
                throw new SQLException("Please use the prepare() method to prepare a query.");
        }
    }

    @Override
    public StatementContract getStatement(String query) throws SQLException
    {
        String[] statement = query.trim().split(" ", 2);

        try {
            return MySQLStatement.valueOf(statement[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SQLException(String.format("Unknown statement: \"%s\".", statement[0]));
        }
    }

    @Override
    public boolean isTable(String table)
    {
        Statement statement;

        try {
            statement = getConnection().createStatement();
        } catch (SQLException e) {
            return false;
        }

        try {
            statement.executeQuery(String.format("SELECT * FROM `%s` LIMIT 1;", table));

            return true;
        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public boolean truncate(String table)
    {
        try {
            if (!isTable(table)) {
                return false;
            }

            try (Statement statement = getConnection().createStatement()) {
                statement.executeUpdate(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException e) {
        }

        return false;
    }
}
