package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public record PearFixedStringExpression(String stringValue) implements PearValueExpression {
    @Override
    public PearValue<String> evaluate(PearContext context) {
        return new PearValue<>(String.class, stringValue);
    }
}
