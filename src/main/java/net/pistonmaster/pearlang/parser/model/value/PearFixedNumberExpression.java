package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public record PearFixedNumberExpression(Number numberValue) implements PearValueExpression {
    @Override
    public PearValue<Number> evaluate(PearContext context) {
        return new PearValue<>(Number.class, numberValue);
    }
}
