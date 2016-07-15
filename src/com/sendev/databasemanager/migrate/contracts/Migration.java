package com.sendev.databasemanager.migrate.contracts;

import java.sql.SQLException;

import com.sendev.databasemanager.schema.Schema;

public interface Migration
{
    /**
     * Gets the time the migration was created at, this is used to order
     * migrations, making sure migrations are rolled out to the
     * database and back in the right order.
     * <p>
     * The time format can be any of the supported carbon time formats.
     *
     * @see com.sendev.databasemanager.utils.Carbon
     *
     * @return the carbon time string
     */
    public String created_at();

    /**
     * Attempts to migrate the database, this is automatically executed from the
     * {@link com.sendev.databasemanager.migrate.Migrations#up() migrate up} method.
     *
     * @param schema the database schematic instance
     *
     * @return the result of the schematic instance call
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean up(Schema schema) throws SQLException;

    /**
     * Attempts to rollback the mgirations from the database, this is automatically executed from the
     * {@link com.sendev.databasemanager.migrate.Migrations#down() down()} and
     * {@link com.sendev.databasemanager.migrate.Migrations#rollback(int) rollback(int)} method.
     *
     * @param schema the database schematic instance
     *
     * @return the result of the schematic instance call
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean down(Schema schema) throws SQLException;
}
