package com.sendev.databasemanager.query;

public enum OperatorType
{

    AND("AND"),
    OR("OR");

    private final String operator;

    private OperatorType(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
}
