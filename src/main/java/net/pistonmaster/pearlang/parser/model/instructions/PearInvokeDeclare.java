package net.pistonmaster.pearlang.parser.model.instructions;

import net.pistonmaster.pearlang.executor.PearContext;
import net.pistonmaster.pearlang.executor.PearValue;
import net.pistonmaster.pearlang.executor.PearVariableState;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

import java.util.List;
import java.util.Optional;

public record PearInvokeDeclare(String functionName,
                                List<PearValueExpression> parameters) implements PearCodeExpression, PearValueExpression {
    @Override
    public PearValue<?> evaluate(PearContext context) {
        return invoke(context).orElseThrow(() -> new RuntimeException("Function " + functionName + " did not return a returnValue"));
    }

    public Optional<PearValue<?>> invoke(PearContext context) {
        PearVariableState<?> functionVariable = context.lastVariableByName(functionName);
        PearValue<?> functionValue = functionVariable.value();
        if (!functionValue.isFunction()) {
            throw new RuntimeException("Variable " + functionName + " is not a function");
        }

        PearFunctionDeclare function = functionValue.asFunction();
        if (function.parameters().size() != parameters.size()) {
            throw new RuntimeException("Function " + functionName + " expects " + function.parameters().size() + " parameters, but got " + parameters.size());
        }

        PearContext newContext = context.copy();
        for (int i = 0, size = this.parameters.size(); i < size; i++) {
            PearValueExpression parameter = this.parameters.get(i);
            String name = function.parameters().get(i).name();

            newContext.addVariable(new PearVariableState<>(name, parameter.evaluate(context)));
        }

        return context.executor().executeInstructions(function.body(), newContext);
    }
}
