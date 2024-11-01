package net.pistonmaster.pearlang.parser.model;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;
import net.pistonmaster.pearlang.parser.model.value.PearVariableReference;
import net.pistonmaster.pearlang.utils.PearNumberUtils;

public enum PearUnaryOperator {
    MINUS {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isNumber()) {
                return new PearValue<>(Number.class, PearNumberUtils.multiply(-1, value.asNumber()));
            } else {
                throw new RuntimeException("Cannot negate " + value);
            }
        }
    },
    PLUS {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            return value;
        }
    },
    NOT {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isBoolean()) {
                return new PearValue<>(boolean.class, !value.asBoolean());
            } else {
                throw new RuntimeException("Cannot negate " + value);
            }
        }
    },
    INCREMENT_PRE {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                var newValue = PearNumberUtils.add(value.asNumber(), 1);
                PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                context.lastVariableByName(variableReference.variableName()).value(pearValue);
                return pearValue;
            } else {
                throw new RuntimeException("Cannot increment " + value);
            }
        }
    },
    DECREMENT_PRE {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                var newValue = PearNumberUtils.add(value.asNumber(), -1);
                PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                context.lastVariableByName(variableReference.variableName()).value(pearValue);
                return pearValue;
            } else {
                throw new RuntimeException("Cannot decrement " + value);
            }
        }
    },
    INCREMENT_POST {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                var newValue = PearNumberUtils.add(value.asNumber(), 1);
                PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                context.lastVariableByName(variableReference.variableName()).value(pearValue);
                return value;
            } else {
                throw new RuntimeException("Cannot increment " + value);
            }
        }
    },
    DECREMENT_POST {
        @Override
        public PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression) {
            if (value.isNumber() && expression instanceof PearVariableReference variableReference) {
                var newValue = PearNumberUtils.add(value.asNumber(), -1);
                PearValue<?> pearValue = new PearValue<>(Number.class, newValue);
                context.lastVariableByName(variableReference.variableName()).value(pearValue);
                return value;
            } else {
                throw new RuntimeException("Cannot decrement " + value);
            }
        }
    };

    public abstract PearValue<?> evaluate(PearValue<?> value, PearContext context, PearValueExpression expression);
}
