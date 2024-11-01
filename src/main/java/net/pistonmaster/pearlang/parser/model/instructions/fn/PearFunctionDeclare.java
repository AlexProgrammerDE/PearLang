package net.pistonmaster.pearlang.parser.model.instructions.fn;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

import java.util.List;

public record PearFunctionDeclare(List<PearFunctionParameter> parameters,
                                  List<PearCodeExpression> body) implements PearCodeExpression, PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        return new PearValue<>(PearFunctionDeclare.class, this);
    }
}
