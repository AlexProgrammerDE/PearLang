package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public record PearFixedNullExpression() implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        return new PearValue<>(null, null);
    }
}
