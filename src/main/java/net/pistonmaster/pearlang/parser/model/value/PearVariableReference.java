package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public record PearVariableReference(String variableName) implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        return context.lastVariableByName(variableName).value().copy();
    }
}
