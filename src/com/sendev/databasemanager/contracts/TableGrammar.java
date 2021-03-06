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
        boolean first = true;

        for (QueryClause obj : builder.getWhereClauses()) {
            // This will build a normal clause
            if (obj instanceof Clause) {
                Clause clause = (Clause) obj;

                addClause(clause, first);
                first = false;

                continue;
            }

            // This will build a nested clause
            if (obj instanceof NestedClause) {
                NestedClause nestedClause = (NestedClause) obj;

                if (nestedClause.getWhereClauses().isEmpty()) {
                    continue;
                }

                first = true;
                addPart(" %s (", nestedClause.getOperator());

                for (QueryClause temp : nestedClause.getWhereClauses()) {
                    if (!(temp instanceof Clause)) {
                        continue;
                    }

                    Clause clause = (Clause) temp;

                    addClause(clause, first);
                    first = false;
                }

                addPart(") ");
            }

            first = false;
        }
    }

    private void addClause(Clause clause, boolean exemptOperator)
    {
        if (clause.getOrder() == null) {
            clause.setOrder(OperatorType.AND);
        }

        String field = clause.getTwo().toString();
        if (!isNumeric(field)) {
            field = String.format("'%s'", field);
        }

        String stringClause = String.format("%s %s %s", formatField(clause.getOne()), clause.getIdentifier(), field);

        String operator = "";
        if (!exemptOperator) {
            operator = clause.getOrder().getOperator() + " ";
        }

        addRawPart(String.format("%s%s ", operator, stringClause));
    }
}
