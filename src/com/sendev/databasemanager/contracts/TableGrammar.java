package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.query.Clause;
import com.sendev.databasemanager.query.NestedClause;
import com.sendev.databasemanager.query.OperatorType;
import com.sendev.databasemanager.query.QueryBuilder;

public abstract class TableGrammar extends Grammar
{
    /**
     * The query formatter method, this is called by the query
     * builder when the query should be built.
     *
     * @param builder The query builder to format.
     *
     * @return the formatted query
     */
    public abstract String format(QueryBuilder builder);

    /**
     * Adds the last few touches the query needs to be ready to be executed.
     *
     * @param builder The query builder to finalize.
     *
     * @return the finalized query
     */
    protected abstract String finalize(QueryBuilder builder);

    /**
     * builds the where clauses for the provided query builder.
     *
     * @param builder the query builder to build the where clauses from
     */
    protected void buildWhereClause(QueryBuilder builder)
    {
        if (builder.getWhereClauses().isEmpty()) {
            return;
        }

        addPart(" WHERE ");
        int orderLength = 0;

        for (QueryClause obj : builder.getWhereClauses()) {
            // This will build a normal clause
            if (obj instanceof Clause) {
                Clause clause = (Clause) obj;

                orderLength = addClause(clause);

                continue;
            }

            // This will build a nested clause
            if (obj instanceof NestedClause) {
                NestedClause nestedClause = (NestedClause) obj;

                if (nestedClause.getWhereClauses().isEmpty()) {
                    continue;
                }

                removeLast(orderLength);
                addPart(" %s (", nestedClause.getOperator());

                for (QueryClause temp : nestedClause.getWhereClauses()) {
                    if (!(temp instanceof Clause)) {
                        continue;
                    }

                    Clause clause = (Clause) temp;

                    orderLength = addClause(clause);
                }
                String operator = query.substring(query.length() - orderLength, query.length());

                removeLast(orderLength);

                addPart(") %s ", operator.trim());
            }
        }

        if (orderLength > 0) {
            removeLast(orderLength);
        }
    }

    private int addClause(Clause clause)
    {
        String string = String.format("%s %s",
        formatField(clause.getOne()), clause.getIdentifier()
        );

        if (clause.getOrder() == null) {
            clause.setOrder(OperatorType.AND);
        }

        String field = clause.getTwo().toString();
        if (!isNumeric(field)) {
            field = String.format("'%s'", field);
        }

        String operator = clause.getOrder().getOperator();

        addRawPart(String.format(string + " %s %s ", field, operator));

        return operator.length() + 2;
    }
}
