package com.sendev.databasemanager.migrate;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.migrate.contracts.Migration;
import com.sendev.databasemanager.query.QueryBuilder;
import com.sendev.databasemanager.schema.Blueprint;
import com.sendev.databasemanager.utils.Collection;
import com.sendev.databasemanager.utils.DataRow;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migrations
{
    private final DatabaseManager dbm;
    private final List<MigrationContainer> migrations;

    private final String TABLE_NAME = "dbm_migrations";
    private boolean ranSetup = false;

    public Migrations(DatabaseManager dbm)
    {
        this.dbm = dbm;

        this.migrations = new ArrayList<>();
    }

    /**
     * Registers a list of migrations to the migration containers, the migrations
     * will be used in {@link #up() up()}, {@link #down() down()} and {@link #rollback(int) rollback(int)}
     * <p>
     * All migrations must follow the {@link com.sendev.databasemanager.migrate.contracts.Migration Migration contract}.
     *
     * @see com.sendev.databasemanager.migrate.contracts.Migration
     *
     * @param migration the list of migrations that should be registered
     */
    public void register(Migration... migration)
    {
        String plugin = dbm.plugin().getName();

        ENTIRE_LOOP:
        for (Migration migrate : migration) {
            for (MigrationContainer container : migrations) {
                if (container.match(plugin, migrate)) {
                    container.setMigration(migrate);

                    continue ENTIRE_LOOP;
                }
            }

            migrations.add(new MigrationContainer(plugin, migrate));
        }
    }

    /**
     * Checks all the migrations in the container against the migrations in the database, filters
     * out the migrations that have already been run before and then runs the new migrations
     * against the default database connection.
     * <p>
     * If the DBM migrations table isn't found in the database, the table will be created.
     *
     * @return either (1) true if at-least one migration was migrated to the database successfully
     *         or (2) false if nothing was migrated to the database.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean up() throws SQLException
    {
        checkAndRunMigrationSetup();

        String plugin = dbm.plugin().getName();

        updateBatchForLocalMigrations(plugin);

        boolean ranMigrations = false;
        for (MigrationContainer migration : getOrderedMigrations(true)) {
            if (migration.getBatch() == 1) {
                continue;
            }

            migration.getMigration().up(dbm.schema());
            updateRemoteMigrationBatchValue(plugin, migration, 1);

            dbm.output().info("Migration: Created \"%s::%s\"", plugin, migration.getName());

            ranMigrations = true;
        }

        if (!ranMigrations) {
            dbm.output().info("Migration: There were nothing to migrate");
        }

        return ranMigrations;
    }

    /**
     * Checks all the migrations in the container against the migrations in the database, filters
     * out the migrations that haven't been executed before and then rolls back all the
     * existing migrations from default database connection.
     * <p>
     * If the DBM migrations table isn't found in the database, the table will be created.
     *
     * @return either (1) true if at-least one migration was rolled back from the database successfully
     *         or (2) false if nothing was rolled back from the database.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean down() throws SQLException
    {
        checkAndRunMigrationSetup();

        String plugin = dbm.plugin().getName();

        updateBatchForLocalMigrations(plugin);

        boolean ranMigrations = false;
        for (MigrationContainer migration : getOrderedMigrations(false)) {
            if (migration.getBatch() != 1) {
                continue;
            }

            migration.getMigration().down(dbm.schema());
            updateRemoteMigrationBatchValue(plugin, migration, 0);

            dbm.output().info("Migration: Rolled back \"%s::%s\"", plugin, migration.getName());

            ranMigrations = true;
        }

        if (!ranMigrations) {
            dbm.output().info("Migration: There were nothing to rollback");
        }

        return ranMigrations;
    }

    /**
     * Checks all the migrations in the container against the migrations in the database, filters
     * out the migrations that haven't been executed before and then rolls back all the
     * existing migrations from default database connection.
     * <p>
     * If the DBM migrations table isn't found in the database, the table will be created.
     *
     * @param steps the amount of steps to rollback
     *
     * @return either (1) true if at-least one migration was rolled back from the database successfully
     *         or (2) false if nothing was rolled back from the database.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public boolean rollback(int steps) throws SQLException
    {
        checkAndRunMigrationSetup();

        String plugin = dbm.plugin().getName();

        updateBatchForLocalMigrations(plugin);

        int ran = 0;
        boolean ranMigrations = false;
        for (MigrationContainer migration : getOrderedMigrations(false)) {
            if (migration.getBatch() != 1) {
                continue;
            }

            if (ran++ >= steps) {
                break;
            }

            migration.getMigration().down(dbm.schema());
            updateRemoteMigrationBatchValue(plugin, migration, 0);

            dbm.output().info("Migration: Rolled back \"%s::%s\"", plugin, migration.getName());

            ranMigrations = true;
        }

        if (!ranMigrations) {
            dbm.output().info("Migration: There were nothing to rollback");
        }

        return ranMigrations;
    }

    /**
     * Gets a ordered list of the migration containers.
     *
     * @param orderByAsc determines if the list should be ordered ascending or descendingly
     *
     * @return the ordered migration list
     */
    public List<MigrationContainer> getOrderedMigrations(boolean orderByAsc)
    {
        List<MigrationContainer> orderedMigrations = new ArrayList<>(migrations);

        Collections.sort(orderedMigrations, new MigrationComparator(orderByAsc));

        return orderedMigrations;
    }

    public List<MigrationContainer> getMigrations()
    {
        return migrations;
    }

    private void checkAndRunMigrationSetup() throws SQLException
    {
        if (ranSetup) {
            return;
        }

        runMigrationSetup();
        ranSetup = true;
    }

    private void runMigrationSetup() throws SQLException
    {
        boolean created = dbm.schema().createIfNotExists(TABLE_NAME, ( Blueprint table ) -> {
            table.String("plugin");
            table.String("migration");
            table.Integer("batch");
        }, true);

        if (created) {
            dbm.output().info("Migration table created successfully");
        }
    }

    private void updateBatchForLocalMigrations(String plugin) throws SQLException
    {
        Collection results = dbm.query(makeQuery().where("plugin", plugin));
        Map<String, Integer> batchMigrations = new HashMap<>();

        for (DataRow row : results) {
            batchMigrations.put(row.getString("migration"), row.getInt("batch"));
        }

        migrations.stream().filter(( container ) -> (batchMigrations.containsKey(container.getName()))).forEach(( container ) -> {
            container.setBatch(batchMigrations.get(container.getName()));
        });
    }

    private void updateRemoteMigrationBatchValue(String plugin, MigrationContainer migration, int batch) throws SQLException
    {
        Map<String, Object> item = new HashMap<>();
        item.put("batch", batch);

        // If the migration has ran before, but was rolled back(down), this will update the existing row
        if (migration.getBatch() != -1) {
            makeQuery().where("plugin", plugin).andWhere("migration", migration.getName()).update(item);
        } // If the migration has never run before, this will create a new row
        else {
            item.put("plugin", plugin);
            item.put("migration", migration.getName());

            makeQuery().insert(item);
        }

        migration.setBatch(batch);
    }

    private QueryBuilder makeQuery()
    {
        return new QueryBuilder(TABLE_NAME, true);
    }
}
