package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.query.Clause;
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
     * @return String
     */
    public abstract String format(QueryBuilder builder);

    /**
     * Adds the last few touches the query needs to be ready to be executed.
     *
     * @param builder The query builder to finalize.
     *
     * @return String
     */
    protected abstract String finalize(QueryBuilder builder);

    protected void buildWhereClause(QueryBuilder builder)
    {
        if (builder.getWhereClauses().isEmpty()) {
            return;
        }

        addPart(" WHERE");
        int orderLength = 0;

        for (Clause clause : builder.getWhereClauses()) {

            String string = String.format(" %s %s",
            /*
             * >_>
             */ formatField(clause.getOne()), clause.getIdentifier()
            );

            if (clause.getOrder() == null) {
                clause.setOrder(OperatorType.AND);
            }

            String field = clause.getTwo().toString();
            if (!isNumeric(field)) {
                field = String.format("'%s'", field);
            }

            String operator = clause.getOrder().getOperator();

            orderLength = operator.length() + 2;
            addPart(String.format(string + " %s %s ", field, operator));
        }

        if (orderLength > 0) {
            removeLast(orderLength);
        }
    }
}
