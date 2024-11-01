package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.PearUnaryOperator;

public record PearUnaryExpression(PearValueExpression expression,
                                  PearUnaryOperator operator) implements PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        var value = expression.evaluate(context);

        return operator.evaluate(value, context, expression);
    }
}
