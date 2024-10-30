package net.pistonmaster.pearlang.parser.model.value;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.PearExpression;

public interface PearValueExpression extends PearExpression {
    PearValue<?> evaluate(PearContext context);
}
