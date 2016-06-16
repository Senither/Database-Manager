package com.sendev.databasemanager.query;

public class Clause
{

    private final String one;
    private final String identifier;
    private final Object two;
    private OperatorType order;

    public Clause(String one, String identifier, Object two)
    {
        this.one = one;
        this.identifier = identifier;
        this.two = two;

        this.order = OperatorType.AND;
    }

    public Clause(String one, String identifier, Object two, OperatorType order)
    {
        this.one = one;
        this.identifier = identifier;
        this.two = two;

        this.order = order;
    }

    public String getOne()
    {
        return one;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public Object getTwo()
    {
        return two;
    }

    public OperatorType getOrder()
    {
        return order;
    }

    public void setOrder(OperatorType order)
    {
        this.order = order;
    }
}
