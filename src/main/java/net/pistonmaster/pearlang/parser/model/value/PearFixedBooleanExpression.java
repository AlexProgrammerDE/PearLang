package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public record PearFixedBooleanExpression(boolean booleanValue) implements PearValueExpression {
    @Override
    public PearValue<Boolean> evaluate(PearContext context) {
        return new PearValue<>(boolean.class, booleanValue);
    }
}
