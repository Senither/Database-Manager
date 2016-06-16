package com.sendev.databasemanager.query;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.contracts.TableGrammar;
import com.sendev.databasemanager.exceptions.DatabaseException;
import com.sendev.databasemanager.utils.Collection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class QueryBuilder
{

    private QueryType type;

    private String table = null;

    private int take = -1;
    private int skip = -1;

    private final List<QueryOrder> order = new ArrayList<>();

    private final List<Clause> wheres = new ArrayList<>();

    private final List<String> columns = new ArrayList<>();

    private final List<JoinClause> joins = new ArrayList<>();

    private final List<Map<String, Object>> items = new ArrayList<>();

    public QueryBuilder()
    {
    }

    public QueryBuilder(String table)
    {
        table(table);
    }

    public QueryBuilder table(String table)
    {
        return selectAll().from(table);
    }

    public QueryBuilder from(String table)
    {
        this.table = table;

        return this;
    }

    public String getTable()
    {
        return table;
    }

    public QueryBuilder selectAll()
    {
        return select("*");
    }

    public QueryBuilder select(String... colums)
    {
        type = QueryType.SELECT;

        for (String column : colums) {
            addColumn(column);
        }

        return this;
    }

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

    public List<String> getColumns()
    {
        return columns;
    }

    public QueryBuilder limit(int limit)
    {
        this.take = Math.max(limit, 0);

        return this;
    }

    public QueryBuilder noLimit()
    {
        return limit(0);
    }

    public int getLimit()
    {
        return take;
    }

    public QueryBuilder skip(int skip)
    {
        this.skip = Math.max(skip, 0);

        return this;
    }

    public QueryBuilder removeSkip()
    {
        this.skip = -1;

        return this;
    }

    public int getSkip()
    {
        return skip;
    }

    public QueryBuilder take(int take)
    {
        this.take = Math.max(take, 0);

        return this;
    }

    public QueryBuilder removeTake()
    {
        this.take = -1;

        return this;
    }

    public int getTake()
    {
        return take;
    }

    public QueryBuilder where(String column, Object field)
    {
        return where(column, "=", field);
    }

    public QueryBuilder where(String column, String identifier, Object field)
    {
        wheres.add(new Clause(column, identifier, field));

        return this;
    }

    public QueryBuilder andWhere(String column, Object field)
    {
        return andWhere(column, "=", field);
    }

    public QueryBuilder andWhere(String column, String identifier, Object field)
    {
        wheres.add(new Clause(column, identifier, field, OperatorType.AND));

        return this;
    }

    public QueryBuilder orWhere(String column, Object field)
    {
        return orWhere(column, "=", field);
    }

    public QueryBuilder orWhere(String column, String identifier, Object field)
    {
        wheres.add(new Clause(column, identifier, field, OperatorType.OR));

        return this;
    }

    public List<Clause> getWhereClauses()
    {
        return wheres;
    }

    public QueryBuilder orderBy(String field)
    {
        return orderBy(field, "ASC");
    }

    public QueryBuilder orderBy(String field, String type)
    {
        order.add(new QueryOrder(field, type));

        return this;
    }

    public QueryBuilder inRandomOrder()
    {
        order.add(new QueryOrder("RAND()", null, true));

        return this;
    }

    public List<QueryOrder> getOrder()
    {
        return order;
    }

    public JoinClause join(String table, String type)
    {
        JoinClause join = new JoinClause(type, table);

        joins.add(join);

        return join;
    }

    public JoinClause leftJoin(String table)
    {
        return join(table, "left");
    }

    public QueryBuilder leftJoin(String table, String one, String two)
    {
        JoinClause join = leftJoin(table);

        join.on(one, two);

        return this;
    }

    public QueryBuilder leftJoin(String table, String one, String identifier, String two)
    {
        JoinClause join = leftJoin(table);

        join.on(one, identifier, two);

        return this;
    }

    public JoinClause rightJoin(String table)
    {
        return join(table, "right");
    }

    public QueryBuilder rightJoin(String table, String one, String two)
    {
        JoinClause join = rightJoin(table);

        join.on(one, two);

        return this;
    }

    public QueryBuilder rightJoin(String table, String one, String identifier, String two)
    {
        JoinClause join = rightJoin(table);

        join.on(one, identifier, two);

        return this;
    }

    public JoinClause innerJoin(String table)
    {
        return join(table, "inner");
    }

    public QueryBuilder innerJoin(String table, String one, String two)
    {
        JoinClause join = innerJoin(table);

        join.on(one, two);

        return this;
    }

    public QueryBuilder innerJoin(String table, String one, String identifier, String two)
    {
        JoinClause join = innerJoin(table);

        join.on(one, identifier, two);

        return this;
    }

    public JoinClause outerJoin(String table)
    {
        return join(table, "outer");
    }

    public QueryBuilder outerJoin(String table, String one, String two)
    {
        JoinClause join = outerJoin(table);

        join.on(one, two);

        return this;
    }

    public QueryBuilder outerJoin(String table, String one, String identifier, String two)
    {
        JoinClause join = outerJoin(table);

        join.on(one, identifier, two);

        return this;
    }

    public JoinClause fullJoin(String table)
    {
        return join(table, "full");
    }

    public QueryBuilder fullJoin(String table, String one, String two)
    {
        JoinClause join = fullJoin(table);

        join.on(one, two);

        return this;
    }

    public QueryBuilder fullJoin(String table, String one, String identifier, String two)
    {
        JoinClause join = fullJoin(table);

        join.on(one, identifier, two);

        return this;
    }

    public List<JoinClause> getJoins()
    {
        return joins;
    }

    public QueryBuilder insert(Map<String, Object>... items)
    {
        type = QueryType.INSERT;

        this.items.addAll(Arrays.asList(items));

        return this;
    }

    public QueryBuilder insert(List<String>... arrays)
    {
        return insert(buildMapFromArrays(arrays));
    }

    public QueryBuilder update(Map<String, Object>... items)
    {
        type = QueryType.UPDATE;

        this.items.addAll(Arrays.asList(items));

        return this;
    }

    public QueryBuilder update(List<String>... arrays)
    {
        return update(buildMapFromArrays(arrays));
    }

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

    public List<Map<String, Object>> getItems()
    {
        return items;
    }

    public QueryBuilder delete()
    {
        type = QueryType.DELETE;

        return this;
    }

    public String toSQL()
    {
        try {
            TableGrammar grammar = (TableGrammar) type.getGrammar().newInstance();

            String sql = grammar.format(this);

            return sql;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(QueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Collection get() throws SQLException
    {
        DatabaseManager dbm = DatabaseFactory.getDynamicOrigin(getClass());

        if (dbm == null) {
            throw new DatabaseException("Failed to find any data binding connected to the instantiated class.");
        }

        return dbm.query(this);
    }

    @Override
    public String toString()
    {
        return toSQL();
    }
}
