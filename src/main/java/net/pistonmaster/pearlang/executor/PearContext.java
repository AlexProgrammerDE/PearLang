package net.pistonmaster.pearlang.executor;

import net.pistonmaster.pearlang.parser.model.instructions.PearNativeCodeExpression;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionParameter;

import java.util.ArrayList;
import java.util.List;

public record PearContext(PearExecutor executor, List<PearVariableState<?>> variables) {
    public static PearContext createDefault(PearExecutor executor) {
        return new PearContext(executor, new ArrayList<>(List.of(new PearVariableState<>("println", new PearValue<>(PearFunctionDeclare.class, new PearFunctionDeclare(
                List.of(new PearFunctionParameter("text")),
                List.of((PearNativeCodeExpression) context -> {
                    System.out.println(context.lastVariableByName("text").value().asString());
                    return null;
                }))
        )))));
    }

    public PearVariableState<?> lastVariableByName(String name) {
        return variables.stream().filter(variable -> variable.name().equals(name)).reduce((a, b) -> b)
                .orElseThrow(() -> new RuntimeException("Variable " + name + " not found"));
    }

    public void addVariable(PearVariableState<?> variable) {
        variables.add(variable);
    }

    public PearContext copy() {
        return new PearContext(executor, new ArrayList<>(variables));
    }
}
