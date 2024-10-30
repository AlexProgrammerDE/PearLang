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
        if (functionName.equals("println")) {
            executePrintln(context);
            return Optional.empty();
        }

        PearFunctionDeclare function = context.functionByName(functionName);
        if (function.parameters().size() != parameters.size()) {
            throw new RuntimeException("Function " + functionName + " expects " + function.parameters().size() + " parameters, but got " + parameters.size());
        }

        PearContext newContext = context.copy();
        int i = 0;
        for (PearValueExpression parameter : this.parameters) {
            String name = function.parameters().get(i).name();
            newContext.variables().removeIf(variable -> variable.name().equals(name));
            newContext.functions().removeIf(function1 -> function1.name().equals(name));

            newContext.addVariable(new PearVariableState<>(name, parameter.evaluate(context)));
            i++;
        }

        return context.executor().executeInstructions(function.body(), newContext);
    }

    private void executePrintln(PearContext context) {
        System.out.println(parameters.get(0).evaluate(context).value());
    }
}
