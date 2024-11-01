package net.pistonmaster.pearlang.parser.model.instructions;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;

public interface PearNativeCodeExpression extends PearCodeExpression {
    PearValue<?> evaluate(PearContext context);
}
