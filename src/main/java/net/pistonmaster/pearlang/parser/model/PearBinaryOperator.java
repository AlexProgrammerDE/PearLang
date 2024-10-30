package net.pistonmaster.pearlang.parser.model;

import net.pistonmaster.pearlang.reader.PearToken;

public enum PearBinaryOperator {
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MODULO,
    EQUAL,
    NOT_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    AND,
    OR;

    public static PearBinaryOperator fromToken(PearToken token) {
        return switch (token) {
            case PLUS -> PLUS;
            case MINUS -> MINUS;
            case MULTIPLY -> MULTIPLY;
            case DIVIDE -> DIVIDE;
            case MODULO -> MODULO;
            case EQUALS -> EQUAL;
            case NOT_EQUALS -> NOT_EQUAL;
            case GREATER_THAN -> GREATER;
            case GREATER_THAN_OR_EQUAL -> GREATER_EQUAL;
            case LESS_THAN -> LESS;
            case LESS_THAN_OR_EQUAL -> LESS_EQUAL;
            case AND -> AND;
            case OR -> OR;
            default -> throw new IllegalArgumentException("Token " + token + " is not a valid binary operator");
        };
    }
}
