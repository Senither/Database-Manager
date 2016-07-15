package com.sendev.databasemanager.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.DatabaseOriginLookup;
import com.sendev.databasemanager.contracts.QueryClause;
import com.sendev.databasemanager.exceptions.DatabaseException;
import com.sendev.databasemanager.exceptions.OriginException;
import com.sendev.databasemanager.grammar.GrammarParser;
import com.sendev.databasemanager.utils.Collection;

public final class QueryBuilder implements DatabaseOriginLookup
{

    /**
     * The query type that's being preformed.
     */
    private QueryType type;

    /**
     * The table the query builder to used.
     */
    private String table = null;

    /**
     * The amount of rows to take(LIMIT), if the value is set
     * to -1 it should be ignored by the grammar generator.
     */
    private int take = -1;

    /**
     * The amount of rows to skip(OFFSET), if the value is set
     * to -1 it should be ignored by the grammar generator.
     */
    private int skip = -1;

    /**
     * The list of {@link QueryOrder} clauses that should be used by the generator.
     */
    private final List<QueryOrder> order = new ArrayList<>();

    /**
     * The list of {@link Clause} clauses that should be used by the generator.
     */
    private final List<QueryClause> wheres = new ArrayList<>();

    /**
     * The list of {@link String} clauses that should be used by the generator.
     */
    private final List<String> columns = new ArrayList<>();

    /**
     * The list of {@link JoinClause} objects that should be used by the generator.
     */
    private final List<JoinClause> joins = new ArrayList<>();

    /**
     * The list of {@link Map} objects that should be used by the generator, containing
     * the column name as the key, and value for the column as a map value.
     */
    private final List<Map<String, Object>> items = new ArrayList<>();

    /**
     * Determines if the query grammar generator should ignore the database prefix.
     */
    private final boolean ignoreDatabasePrefix;

    /**
     * Creates a new Query Builder instance with no table preset, that
     * doesn't ignores database prefixes.
     */
    public QueryBuilder()
    {
        this.ignoreDatabasePrefix = false;
    }

    /**
     * Creates a new Query builder instance with no table preset.
     *
     * @param ignoreDatabasePrefix Determines if the database prefix should
     *                             be ignored during the query generation or not
     */
    public QueryBuilder(boolean ignoreDatabasePrefix)
    {
        this.ignoreDatabasePrefix = ignoreDatabasePrefix;
    }

    /**
     * Creates a new Query Builder instance for the provided table, that
     * ignores database prefixes.
     * <p>
     * This calls the {@link #table(java.lang.String) } method behind the scene.
     *
     * @param table The table the query builder should be generated for
     */
    public QueryBuilder(String table)
    {
        this.ignoreDatabasePrefix = false;

        table(table);
    }

    /**
     * Creates a new Query Builder instance for the provided table.
     * <p>
     * This calls the {@link #table(java.lang.String) } method behind the scene.
     *
     * @param table                The table the query builder should be generated for
     * @param ignoreDatabasePrefix Determines if the database prefix should
     *                             be ignored during the query generation or not
     */
    public QueryBuilder(String table, boolean ignoreDatabasePrefix)
    {
        this.ignoreDatabasePrefix = ignoreDatabasePrefix;

        table(table);
    }

    /**
     * Determines if the query builder should ignore
     * the database prefix when generating the query.
     *
     * @return if the query builder should ignore the database prefix when generating the query.
     */
    public boolean isIgnoringDatabasePrefix()
    {
        return ignoreDatabasePrefix;
    }

    /**
     * Sets the table that the query builder should be using, and sets the select state to ALL.
     * This is the same as calling {@link #selectAll() } and {@link #from(java.lang.String) } in one go.
     *
     * @param table The table the query builder should be using
     *
     * @return the query builder instance.
     */
    public QueryBuilder table(String table)
    {
        return selectAll().from(table);
    }

    /**
     * Sets the table the query should be generated for.
     *
     * @param table The table the query should be generated for
     *
     * @return the query builder instance.
     */
    public QueryBuilder from(String table)
    {
        this.table = table;

        return this;
    }

    /**
     * Gets the table the query should be generated for.
     *
     * @return the table the query should be generated for.
     */
    public String getTable()
    {
        return table;
    }

    /**
     * Sets the select state to ALL using the star(*) symbol.
     *
     * @return the query builder instance.
     */
    public QueryBuilder selectAll()
    {
        return select("*");
    }

    /**
     * Selects the provided columns from the varargs columns object, columns
     * parsed will automatically be formatted to SQL fields using the grave
     * accent character(`), using the keyword <code>AS</code> will use the
     * SQL AS to rename the output the collection will end up using.
     * <p>
     * Example calling: <code>select("username as name", "email", ...)</code>
     * <p>
     * Would re-name the username field in the table to name in the collection object.
     *
     * @param colums The varargs list of columns that should be selected.
     *
     * @return the query builder instance.
     */
    public QueryBuilder select(String... colums)
    {
        type = QueryType.SELECT;

        for (String column : colums) {
            addColumn(column);
        }

        return this;
    }

    /**
     * Creates a raw select statement, allowing you to parse in raw SQL that
     * the generator won't modify or affect in any way.
     *
     * @param select The raw SQL select statement.
     *
     * @return the query builder instance.
     */
    public QueryBuilder selectRaw(String select)
    {
        type = QueryType.SELECT;

        columns.clear();
        columns.add("RAW:" + select.trim());

        return this;
    }

    /**
     * Adds a column that should be selected.
     *
     * @param column The column that should be selected
     */
    protected void addColumn(String column)
    {
        if (!column.equals("*")) {
            columns.remove("*");

            if (!columns.contains(column)) {
                columns.add(column);
            }

            return;
        }

        columns.clear();
        columns.add("*");
    }

    /**
     * Gets the columns that should be selected in a {@link #get() } request.
     *
     * @return the columns that should be selected.
     */
    public List<String> getColumns()
    {
        return columns;
    }

    /**
     * Sets the amount of rows to skip using the SQL <code>OFFSET</code>.
     *
     * @param skip The amount of rows to skip
     *
     * @return the query builder instance.
     */
    public QueryBuilder skip(int skip)
    {
        this.skip = Math.max(skip, 0);

        return this;
    }

    /**
     * Removes the skip(SQL <code>OFFSET</code>) from the query builder.
     *
     * @return the query builder instance.
     */
    public QueryBuilder removeSkip()
    {
        this.skip = -1;

        return this;
    }

    /**
     * Gets the amount to skip(SQL <code>OFFSET</code>) in the SQL query.
     *
     * @return the amount to skip.
     */
    public int getSkip()
    {
        return skip;
    }

    /**
     * Sets the amount of rows to take using the SQL <code>LIMIT</code>.
     *
     * @param take The amount of rows to take
     *
     * @return the query builder instance.
     */
    public QueryBuilder take(int take)
    {
        this.take = Math.max(take, 0);

        return this;
    }

    /**
     * Removes the take(SQL <code>LIMIT</code>) from the query builder.
     *
     * @return the query builder instance.
     */
    public QueryBuilder removeTake()
    {
        this.take = -1;

        return this;
    }

    /**
     * Gets the amount to take(SQL <code>LIMIT</code>) in the SQL query.
     *
     * @return the amount to take.
     */
    public int getTake()
    {
        return take;
    }

    /**
     * Creates a SQL WHERE clause with an equal operator.
     *
     * @param column The column to use in the clause
     * @param value  The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder where(String column, Object value)
    {
        return where(column, "=", value);
    }

    /**
     * Creates a SQL WHERE clause with the provided operator.
     *
     * @param column   The column to use in the clause
     * @param operator The operator to compare with
     * @param value    The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder where(String column, String operator, Object value)
    {
        wheres.add(new Clause(column, operator, value));

        return this;
    }

    public QueryBuilder where(ClauseConsumer consumer)
    {
        NestedClause clause = new NestedClause();

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }

    /**
     * Creates a SQL AND WHERE clause with an equal operator.
     *
     * @param column The column to use in the clause
     * @param value  The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder andWhere(String column, Object value)
    {
        return andWhere(column, "=", value);
    }

    /**
     * Creates a SQL AND WHERE clause with the provided operator.
     *
     * @param column   The column to use in the clause
     * @param operator The operator to compare with
     * @param value    The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder andWhere(String column, String operator, Object value)
    {
        wheres.add(new Clause(column, operator, value, OperatorType.AND));

        return this;
    }

    public QueryBuilder andWhere(ClauseConsumer consumer)
    {
        NestedClause clause = new NestedClause(OperatorType.AND);

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }

    /**
     * Creates a SQL OR WHERE clause with an equal operator.
     *
     * @param column The column to use in the clause
     * @param value  The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder orWhere(String column, Object value)
    {
        return orWhere(column, "=", value);
    }

    /**
     * Creates a SQL OR WHERE clause with the provided operator.
     *
     * @param column   The column to use in the clause
     * @param operator The operator to compare with
     * @param value    The value to compare the column to
     *
     * @return the query builder instance.
     */
    public QueryBuilder orWhere(String column, String operator, Object value)
    {
        wheres.add(new Clause(column, operator, value, OperatorType.OR));

        return this;
    }

    public QueryBuilder orWhere(ClauseConsumer consumer)
    {
        NestedClause clause = new NestedClause(OperatorType.OR);

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }

    /**
     * Gets the list where clauses that should be generated.
     *
     * @return the list of where clauses that should be generated.
     */
    public List<QueryClause> getWhereClauses()
    {
        return wheres;
    }

    /**
     * Creates a SQL ORDER BY clause, ordering by ascending order.
     *
     * @param field The field the query should be ordered by
     *
     * @return the query builder instance.
     */
    public QueryBuilder orderBy(String field)
    {
        return orderBy(field, "ASC");
    }

    /**
     * Creates a SQL ORDER BY clause, ordering by the provided type.
     *
     * @param field The field the query should be ordered by
     * @param type  The type to order the query by
     *
     * @return the query builder instance.
     */
    public QueryBuilder orderBy(String field, String type)
    {
        order.add(new QueryOrder(field, type));

        return this;
    }

    /**
     * Creates a random query order statement.
     *
     * @return the query builder instance.
     */
    public QueryBuilder inRandomOrder()
    {
        order.add(new QueryOrder("RAND()", null, true));

        return this;
    }

    /**
     * Gets the list of order clauses that should be used in the generated query.
     *
     * @return the list of order clauses.
     */
    public List<QueryOrder> getOrder()
    {
        return order;
    }

    /**
     * Creates a JOIN clause on the provided table of the given type, once the join clause
     * has been created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     * @param type  The type of the join clause
     *
     * @return the join clause that was created.
     */
    public JoinClause join(String table, String type)
    {
        JoinClause join = new JoinClause(type, table);

        joins.add(join);

        return join;
    }

    /**
     * Creates a LEFT JOIN clause on the provided table, once the join clause has been
     * created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     *
     * @return the join clause that was created.
     */
    public JoinClause leftJoin(String table)
    {
        return join(table, "left");
    }

    /**
     * Creates a LEFT JOIN clause on the provided table, using the equal operator.
     *
     * @param table The table the join clause should be used on
     * @param one   The first field to bind on
     * @param two   The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder leftJoin(String table, String one, String two)
    {
        JoinClause join = leftJoin(table);

        join.on(one, two);

        return this;
    }

    /**
     * Creates a LEFT JOIN clause on the provided table, using the provided operator.
     *
     * @param table    The table the join clause should be used on
     * @param one      The first field to bind on
     * @param operator The operator to compare with
     * @param two      The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder leftJoin(String table, String one, String operator, String two)
    {
        JoinClause join = leftJoin(table);

        join.on(one, operator, two);

        return this;
    }

    /**
     * Creates a RIGHT JOIN clause on the provided table, once the join clause has been
     * created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     *
     * @return the join clause that was created.
     */
    public JoinClause rightJoin(String table)
    {
        return join(table, "right");
    }

    /**
     * Creates a RIGHT JOIN clause on the provided table, using the equal operator.
     *
     * @param table The table the join clause should be used on
     * @param one   The first field to bind on
     * @param two   The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder rightJoin(String table, String one, String two)
    {
        JoinClause join = rightJoin(table);

        join.on(one, two);

        return this;
    }

    /**
     * Creates a RIGHT JOIN clause on the provided table, using the provided operator.
     *
     * @param table    The table the join clause should be used on
     * @param one      The first field to bind on
     * @param operator The operator to compare with
     * @param two      The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder rightJoin(String table, String one, String operator, String two)
    {
        JoinClause join = rightJoin(table);

        join.on(one, operator, two);

        return this;
    }

    /**
     * Creates a INNER JOIN clause on the provided table, once the join clause has been
     * created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     *
     * @return the join clause that was created.
     */
    public JoinClause innerJoin(String table)
    {
        return join(table, "inner");
    }

    /**
     * Creates a INNER JOIN clause on the provided table, using the equal operator.
     *
     * @param table The table the join clause should be used on
     * @param one   The first field to bind on
     * @param two   The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder innerJoin(String table, String one, String two)
    {
        JoinClause join = innerJoin(table);

        join.on(one, two);

        return this;
    }

    /**
     * Creates a INNER JOIN clause on the provided table, using the provided operator.
     *
     * @param table    The table the join clause should be used on
     * @param one      The first field to bind on
     * @param operator The operator to compare with
     * @param two      The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder innerJoin(String table, String one, String operator, String two)
    {
        JoinClause join = innerJoin(table);

        join.on(one, operator, two);

        return this;
    }

    /**
     * Creates a OUTER JOIN clause on the provided table, once the join clause has been
     * created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     *
     * @return the join clause that was created.
     */
    public JoinClause outerJoin(String table)
    {
        return join(table, "outer");
    }

    /**
     * Creates a OUTER JOIN clause on the provided table, using the equal operator.
     *
     * @param table The table the join clause should be used on
     * @param one   The first field to bind on
     * @param two   The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder outerJoin(String table, String one, String two)
    {
        JoinClause join = outerJoin(table);

        join.on(one, two);

        return this;
    }

    /**
     * Creates a OUTER JOIN clause on the provided table, using the provided operator.
     *
     * @param table    The table the join clause should be used on
     * @param one      The first field to bind on
     * @param operator The operator to compare with
     * @param two      The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder outerJoin(String table, String one, String operator, String two)
    {
        JoinClause join = outerJoin(table);

        join.on(one, operator, two);

        return this;
    }

    /**
     * Creates a FULL JOIN clause on the provided table, once the join clause has been
     * created, a {@link JoinClause} object will be returned to help specify
     * what the clause should be bound to.
     *
     * @param table The table the join clause should be used on
     *
     * @return the join clause that was created.
     */
    public JoinClause fullJoin(String table)
    {
        return join(table, "full");
    }

    /**
     * Creates a FULL JOIN clause on the provided table, using the equal operator.
     *
     * @param table The table the join clause should be used on
     * @param one   The first field to bind on
     * @param two   The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder fullJoin(String table, String one, String two)
    {
        JoinClause join = fullJoin(table);

        join.on(one, two);

        return this;
    }

    /**
     * Creates a FULL JOIN clause on the provided table, using the provided operator.
     *
     * @param table    The table the join clause should be used on
     * @param one      The first field to bind on
     * @param operator The operator to compare with
     * @param two      The second field to bind on
     *
     * @return the query builder instance.
     */
    public QueryBuilder fullJoin(String table, String one, String operator, String two)
    {
        JoinClause join = fullJoin(table);

        join.on(one, operator, two);

        return this;
    }

    /**
     * Gets a list of all the JOIN clauses.
     *
     * @return a list of all the JOIN clauses.
     */
    public List<JoinClause> getJoins()
    {
        return joins;
    }

    /**
     * Creates the grammar instance and builds the SQL query, if an error occurs
     * while building the query <code>NULL</code> will be returned instead.
     *
     * @return either (1) the generated SQL query
     *         or (2) <code>NULL</code> if an error occurred.
     */
    public String toSQL()
    {
        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        return toSQL(dbm);
    }

    public String toSQL(DatabaseManager dbm)
    {
        try {
            GrammarParser grammar = (GrammarParser) type.getGrammar().newInstance();

            String sql = grammar.parse(dbm, dbm.getConnections().getDefaultConnection(), this);

            return sql;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(QueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#query(java.lang.String, com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>@exception
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public Collection get(String connection) throws SQLException
    {
        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        return dbm.query(connection, this);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#query(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @return a <code>Collection</code> object that contains the data produced
     *         by the given query; never <code>null</code>@exception
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public Collection get() throws SQLException
    {
        return get(null);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param arrays The list of items that should be updated
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int update(List<String>... arrays) throws SQLException
    {
        return update(buildMapFromArrays(arrays));
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(java.lang.String, com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     * @param arrays     The list of items that should be updated
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int update(String connection, List<String>... arrays) throws SQLException
    {
        return update(connection, buildMapFromArrays(arrays));
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param items The map of items that should be updated
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int update(Map<String, Object>... items) throws SQLException
    {
        return update(null, items);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(java.lang.String, com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     * @param items      The map of items that should be updated
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int update(String connection, Map<String, Object>... items) throws SQLException
    {
        type = QueryType.UPDATE;

        this.items.addAll(Arrays.asList(items));

        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        return dbm.queryUpdate(connection, this);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryInsert(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param arrays The list of items that should be inserted
     *
     * @return a Collection
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public Collection insert(List<String>... arrays) throws SQLException
    {
        return insert(buildMapFromArrays(arrays));
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryInsert(java.lang.String, com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     * @param arrays     The list of items that should be inserted
     *
     * @return a Collection of the generated IDs.
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public Collection insert(String connection, List<String>... arrays) throws SQLException
    {
        return insert(connection, buildMapFromArrays(arrays));
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryInsert(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param items The map of items that should be inserted
     *
     * @return a Collection of the generated IDs.
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public Collection insert(Map<String, Object>... items) throws SQLException
    {
        return insert(null, items);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryInsert(java.lang.String, com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     * @param items      The map of items that should be inserted
     *
     * @return a Collection of the generated IDs.
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public Collection insert(String connection, Map<String, Object>... items) throws SQLException
    {
        type = QueryType.INSERT;

        this.items.addAll(Arrays.asList(items));

        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        Set<Integer> keys = dbm.queryInsert(connection, this);
        List<Map<String, Object>> collectionItems = new ArrayList<>();

        for (int id : keys) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", id);
            collectionItems.add(row);
        }

        return new Collection(collectionItems);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int delete() throws SQLException
    {
        return delete(null);
    }

    /**
     * Runs the {@link com.sendev.databasemanager.DatabaseManager#queryUpdate(com.sendev.databasemanager.query.QueryBuilder) }
     * method with the current instance of the query builder.
     *
     * @param connection The database connection to run the query against
     *
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     *
     * @throws SQLException if a database access error occurs;
     *                      this method is called on a closed  <code>PreparedStatement</code>
     *                      or the SQL statement returns a <code>ResultSet</code> object
     */
    public int delete(String connection) throws SQLException
    {
        type = QueryType.DELETE;

        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        return dbm.queryUpdate(this);
    }

    /**
     * Builds a Map from a List object.
     *
     * @param arrays The list to build the map from
     *
     * @return The map that was build from the list.
     */
    private Map<String, Object> buildMapFromArrays(List<String>... arrays)
    {
        Map<String, Object> map = new HashMap<>();

        for (List<String> array : arrays) {
            if (array.size() != 2) {
                continue;
            }

            map.put(array.get(0), array.get(1));
        }

        return map;
    }

    /**
     * Gets the list of item maps for the query builder.
     *
     * @return the list of item maps for the query builder.
     */
    public List<Map<String, Object>> getItems()
    {
        return items;
    }

    @Override
    public String toString()
    {
        return toSQL();
    }

    @Override
    public void throwsOriginException() throws OriginException
    {
        throw new OriginException();
    }
}
