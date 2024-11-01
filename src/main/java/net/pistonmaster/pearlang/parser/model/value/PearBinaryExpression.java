package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.PearBinaryOperator;

public record PearBinaryExpression(PearValueExpression left, PearBinaryOperator operator,
                                   PearValueExpression right) implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        var leftValue = left.evaluate(context);
        var rightValue = right.evaluate(context);
        return operator.evaluate(leftValue, rightValue);
    }
}
