package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.PearBinaryOperator;
import net.pistonmaster.pearlang.utils.PearNumberUtils;

public record PearBinaryExpression(PearValueExpression left, PearBinaryOperator operator,
                                   PearValueExpression right) implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        var leftValue = left.evaluate(context);
        var rightValue = right.evaluate(context);
        switch (operator) {
            case PLUS -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.add(leftValue.asNumber(), rightValue.asNumber()));
                } else if (leftValue.isString() && rightValue.isString()) {
                    return new PearValue<>(String.class, leftValue.asString() + rightValue.asString());
                } else if (leftValue.isString() && rightValue.isNumber()) {
                    return new PearValue<>(String.class, leftValue.asString() + rightValue.asNumber());
                } else if (leftValue.isNumber() && rightValue.isString()) {
                    return new PearValue<>(String.class, leftValue.asNumber() + rightValue.asString());
                } else {
                    throw new RuntimeException("Cannot add " + leftValue + " and " + rightValue);
                }
            }
            case MINUS -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.subtract(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot subtract " + leftValue + " and " + rightValue);
                }
            }
            case MULTIPLY -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.multiply(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot multiply " + leftValue + " and " + rightValue);
                }
            }
            case DIVIDE -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.divide(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot divide " + leftValue + " and " + rightValue);
                }
            }
            case MODULO -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.modulo(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot modulo " + leftValue + " and " + rightValue);
                }
            }
            case EQUAL -> {
                return new PearValue<>(boolean.class, valuesEqual(leftValue, rightValue));
            }
            case NOT_EQUAL -> {
                return new PearValue<>(boolean.class, !valuesEqual(leftValue, rightValue));
            }
            case GREATER -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(boolean.class, PearNumberUtils.greaterThan(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
                }
            }
            case GREATER_EQUAL -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(boolean.class, PearNumberUtils.greaterThanOrEqual(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
                }
            }
            case LESS -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(boolean.class, PearNumberUtils.lessThan(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
                }
            }
            case LESS_EQUAL -> {
                if (leftValue.isNumber() && rightValue.isNumber()) {
                    return new PearValue<>(boolean.class, PearNumberUtils.lessThanOrEqual(leftValue.asNumber(), rightValue.asNumber()));
                } else {
                    throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
                }
            }
            case AND -> {
                if (leftValue.isBoolean() && rightValue.isBoolean()) {
                    return new PearValue<>(boolean.class, leftValue.asBoolean() && rightValue.asBoolean());
                } else {
                    throw new RuntimeException("Cannot AND " + leftValue + " and " + rightValue);
                }
            }
            case OR -> {
                if (leftValue.isBoolean() && rightValue.isBoolean()) {
                    return new PearValue<>(boolean.class, leftValue.asBoolean() || rightValue.asBoolean());
                } else {
                    throw new RuntimeException("Cannot OR " + leftValue + " and " + rightValue);
                }
            }
            default -> throw new RuntimeException("Unknown operator " + operator);
        }
    }

    private boolean valuesEqual(PearValue<?> left, PearValue<?> right) {
        if (left.isNumber() && right.isNumber()) {
            return PearNumberUtils.equals(left.asNumber(), right.asNumber());
        } else if (left.isBoolean() && right.isBoolean()) {
            return left.asBoolean().equals(right.asBoolean());
        } else if (left.isNull() && right.isNull()) {
            return true;
        } else if (left.isString() && right.isString()) {
            return left.asString().equals(right.asString());
        } else {
            throw new RuntimeException("Cannot compare " + left + " and " + right);
        }
    }
}
