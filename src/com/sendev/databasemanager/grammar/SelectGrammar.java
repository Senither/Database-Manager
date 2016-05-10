package com.sendev.databasemanager.grammar;

import com.sendev.databasemanager.contracts.TableGrammar;
import com.sendev.databasemanager.query.Clause;
import com.sendev.databasemanager.query.JoinClause;
import com.sendev.databasemanager.query.OperatorType;
import com.sendev.databasemanager.query.QueryBuilder;
import java.util.List;

public class SelectGrammar extends TableGrammar
{

    public SelectGrammar()
    {
        query = "SELECT ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        buildColumns(builder);

        buildJoins(builder);

        buildWhereClause(builder);

        return finalize(builder);
    }

    private void buildColumns(QueryBuilder builder)
    {
        if (builder.getColumns().size() == 1 && builder.getColumns().get(0).equals("*")) {
            query += "*";
        } else {
            builder.getColumns().stream().forEach(( column ) -> {
                query += formatField(column) + ", ";
            });

            removeLast(2);
        }

        query += String.format(" FROM %s ", formatField(builder.getTable()));
    }

    private void buildJoins(QueryBuilder builder)
    {
        List<JoinClause> joins = builder.getJoins();

        for (JoinClause join : joins) {
            if (join.clauses.isEmpty()) {
                continue;
            }

            addPart(String.format(" %s JOIN %s ON ", join.type.toUpperCase(), formatField(join.table)));

            int orderLength = 0;

            for (Clause clause : join.clauses) {
                String string = String.format(" %s %s %s",
                formatField(clause.getOne()), clause.getIdentifier(), formatField((String) clause.getTwo())
                );

                if (clause.getOrder() == null) {
                    clause.setOrder(OperatorType.AND);
                }

                String operator = clause.getOrder().getOperator();

                orderLength = operator.length() + 2;
                addPart(String.format(string + " %s ", operator));
            }

            if (orderLength > 0) {
                removeLast(orderLength);
            }
        }
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        if (!builder.getOrder().isEmpty()) {
            addPart(" ORDER BY ");

            List<String> orders = builder.getOrder();

            for (int i = 0; i < orders.size(); i++) {
                String field = orders.get(i);

                addPart(String.format(" %s ",
                orderOperators.contains(field.toUpperCase()) ? field.toUpperCase() : formatField(field))
                );

                if ((i + 1) % 2 == 0) {
                    addPart(", ");
                }
            }

            removeLast(2);
        }

        if (builder.getLimit() <= 0) {
            return query.trim() + ";";
        }

        addPart(String.format(" LIMIT %d;", builder.getLimit()));

        return query;
    }
}
