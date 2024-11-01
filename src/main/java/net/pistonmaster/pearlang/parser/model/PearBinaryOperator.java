package net.pistonmaster.pearlang.parser.model;

import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.reader.PearToken;
import net.pistonmaster.pearlang.utils.PearNumberUtils;

public enum PearBinaryOperator {
    PLUS {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
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
    },
    MINUS {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(Number.class, PearNumberUtils.subtract(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot subtract " + leftValue + " and " + rightValue);
            }
        }
    },
    MULTIPLY {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(Number.class, PearNumberUtils.multiply(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot multiply " + leftValue + " and " + rightValue);
            }
        }
    },
    DIVIDE {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(Number.class, PearNumberUtils.divide(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot divide " + leftValue + " and " + rightValue);
            }
        }
    },
    MODULO {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(Number.class, PearNumberUtils.modulo(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot modulo " + leftValue + " and " + rightValue);
            }
        }
    },
    EQUAL {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            return new PearValue<>(boolean.class, valuesEqual(leftValue, rightValue));
        }
    },
    NOT_EQUAL {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            return new PearValue<>(boolean.class, !valuesEqual(leftValue, rightValue));
        }
    },
    GREATER {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(boolean.class, PearNumberUtils.greaterThan(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
            }
        }
    },
    GREATER_EQUAL {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(boolean.class, PearNumberUtils.greaterThanOrEqual(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
            }
        }
    },
    LESS {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(boolean.class, PearNumberUtils.lessThan(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
            }
        }
    },
    LESS_EQUAL {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isNumber() && rightValue.isNumber()) {
                return new PearValue<>(boolean.class, PearNumberUtils.lessThanOrEqual(leftValue.asNumber(), rightValue.asNumber()));
            } else {
                throw new RuntimeException("Cannot compare " + leftValue + " and " + rightValue);
            }
        }
    },
    AND {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isBoolean() && rightValue.isBoolean()) {
                return new PearValue<>(boolean.class, leftValue.asBoolean() && rightValue.asBoolean());
            } else {
                throw new RuntimeException("Cannot AND " + leftValue + " and " + rightValue);
            }
        }
    },
    OR {
        @Override
        public PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue) {
            if (leftValue.isBoolean() && rightValue.isBoolean()) {
                return new PearValue<>(boolean.class, leftValue.asBoolean() || rightValue.asBoolean());
            } else {
                throw new RuntimeException("Cannot OR " + leftValue + " and " + rightValue);
            }
        }
    };

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

    private static boolean valuesEqual(PearValue<?> left, PearValue<?> right) {
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

    public abstract PearValue<?> evaluate(PearValue<?> leftValue, PearValue<?> rightValue);
}
