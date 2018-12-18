package com.tvestergaard.start.data.repositories.base.queries.tree;

public class Conditional
{
    public final Type        type;
    public final Conditional left;
    public final Conditional right;
    public final Operation   operation;

    public Conditional(Type type, Conditional left, Conditional right, Operation operation)
    {
        this.type = type;
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public static Conditional and(Conditional left, Conditional right)
    {
        return new Conditional(Type.AND, left, right, null);
    }

    public static Conditional or(Conditional left, Conditional right)
    {
        return new Conditional(Type.OR, left, right, null);
    }

    public static Conditional op(Operation operation)
    {
        return new Conditional(Type.OP, null, null, operation);
    }

    public enum Type
    {
        AND,
        OR,
        OP
    }
}
