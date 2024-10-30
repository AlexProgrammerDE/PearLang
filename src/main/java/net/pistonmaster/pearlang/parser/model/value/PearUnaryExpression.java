package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.PearUnaryOperator;
import net.pistonmaster.pearlang.utils.PearNumberUtils;

public record PearUnaryExpression(PearValueExpression expression,
                                  PearUnaryOperator operator) implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        var value = expression.evaluate(context);

        switch (operator) {
            case PLUS -> {
                return value;
            }
            case MINUS -> {
                if (value.isNumber()) {
                    return new PearValue<>(Number.class, PearNumberUtils.multiply(-1, value.asNumber()));
                } else {
                    throw new RuntimeException("Cannot negate " + value);
                }
            }
            case NOT -> {
                if (value.isBoolean()) {
                    return new PearValue<>(boolean.class, !value.asBoolean());
                } else {
                    throw new RuntimeException("Cannot negate " + value);
                }
            }
            case INCREMENT_PRE, DECREMENT_PRE -> {
                if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                    var newValue = PearNumberUtils.add(value.asNumber(), operator == PearUnaryOperator.INCREMENT_PRE ? 1 : -1);
                    PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                    context.variableByName(variableReference.variableName()).value(pearValue);
                    return pearValue;
                } else {
                    throw new RuntimeException("Cannot increment/decrement " + value);
                }
            }
            case INCREMENT_POST, DECREMENT_POST -> {
                if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                    var newValue = PearNumberUtils.add(value.asNumber(), operator == PearUnaryOperator.INCREMENT_POST ? 1 : -1);
                    PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                    context.variableByName(variableReference.variableName()).value(pearValue);
                    return value;
                } else {
                    throw new RuntimeException("Cannot increment/decrement " + value);
                }
            }
            default -> throw new RuntimeException("Unknown operator " + operator);
        }
    }
}
