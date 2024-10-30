package net.pistonmaster.pearlang.executor;

import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;

import java.util.ArrayList;
import java.util.List;

public record PearContext(PearExecutor executor, List<PearFunctionDeclare> functions,
                          List<PearVariableState<?>> variables) {
    public PearFunctionDeclare functionByName(String name) {
        return functions.stream().filter(function -> function.name().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("Function " + name + " not found"));
    }

    public PearVariableState<?> variableByName(String name) {
        return variables.stream().filter(variable -> variable.name().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("Variable " + name + " not found"));
    }

    public void addFunction(PearFunctionDeclare function) {
        if (functions.stream().anyMatch(f -> f.name().equals(function.name()))) {
            throw new RuntimeException("Function " + function.name() + " already exists");
        }
        if (variables.stream().anyMatch(v -> v.name().equals(function.name()))) {
            throw new RuntimeException("Variable " + function.name() + " already exists, cannot create function with same name");
        }

        functions.add(function);
    }

    public void addVariable(PearVariableState<?> variable) {
        if (functions.stream().anyMatch(f -> f.name().equals(variable.name()))) {
            throw new RuntimeException("Function " + variable.name() + " already exists, cannot create variable with same name");
        }
        if (variables.stream().anyMatch(v -> v.name().equals(variable.name()))) {
            throw new RuntimeException("Variable " + variable.name() + " already exists");
        }

        variables.add(variable);
    }

    public PearContext copy() {
        return new PearContext(executor, new ArrayList<>(functions), new ArrayList<>(variables));
    }
}
