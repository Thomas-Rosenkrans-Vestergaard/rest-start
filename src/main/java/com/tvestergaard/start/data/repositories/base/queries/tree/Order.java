package com.tvestergaard.start.data.repositories.base.queries.tree;

public class Order
{
    public final String    attribute;
    public final Direction direction;

    public Order(String attribute, Direction direction)
    {
        this.attribute = attribute;
        this.direction = direction;
    }

    public static Order desc(String attribute)
    {
        return new Order(attribute, Direction.DESC);
    }

    public static Order asc(String attribute)
    {
        return new Order(attribute, Direction.ASC);
    }
}
