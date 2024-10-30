package net.pistonmaster.pearlang.executor;

import lombok.RequiredArgsConstructor;
import net.pistonmaster.pearlang.parser.model.PearProgram;
import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.instructions.PearInvokeDeclare;
import net.pistonmaster.pearlang.parser.model.instructions.PearReturn;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;
import net.pistonmaster.pearlang.parser.model.instructions.logic.*;
import net.pistonmaster.pearlang.parser.model.instructions.var.PearVariableAssign;
import net.pistonmaster.pearlang.parser.model.instructions.var.PearVariableDeclare;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PearExecutor {
    private final PearProgram program;
    private final Logger logger = LoggerFactory.getLogger(PearExecutor.class);

    public Optional<Object> execute() {
        return executeInstructions(program.expressions(), new PearContext(this, new ArrayList<>(), new ArrayList<>()))
                .map(PearValue::value);
    }

    public Optional<PearValue<?>> executeInstructions(List<PearCodeExpression> expressions, PearContext localContext) {
        for (PearCodeExpression expression : expressions) {
            Optional<PearValue<?>> returnValue = executeInstruction(expression, localContext);
            if (returnValue.isPresent()) {
                return returnValue;
            }
        }

        return Optional.empty();
    }

    private Optional<PearValue<?>> executeInstruction(PearCodeExpression expression, PearContext localContext) {
        logger.debug("Executing {}", expression.getClass().getSimpleName());
        if (expression instanceof PearFunctionDeclare functionDeclare) {
            localContext.addFunction(functionDeclare);
        } else if (expression instanceof PearVariableDeclare variableDeclare) {
            PearVariableState<?> variableContext = new PearVariableState<>(variableDeclare.name(), evaluateValue(variableDeclare.value(), localContext));
            localContext.addVariable(variableContext);
        } else if (expression instanceof PearReturn returnExpression) {
            return Optional.ofNullable(returnExpression.returnValue()).map(r -> evaluateValue(r, localContext));
        } else if (expression instanceof PearIfDeclare ifDeclare) {
            if (evaluateBooleanValue(ifDeclare.condition(), localContext)) {
                Optional<PearValue<?>> ifReturn = executeInstructions(ifDeclare.ifBody(), localContext.copy());
                if (ifReturn.isPresent()) {
                    return ifReturn;
                }
            }
        } else if (expression instanceof PearIfElseDeclare ifElseDeclare) {
            if (evaluateBooleanValue(ifElseDeclare.condition(), localContext)) {
                Optional<PearValue<?>> ifReturn = executeInstructions(ifElseDeclare.ifBody(), localContext.copy());
                if (ifReturn.isPresent()) {
                    return ifReturn;
                }
            } else {
                Optional<PearValue<?>> elseReturn = executeInstructions(ifElseDeclare.elseBody(), localContext.copy());
                if (elseReturn.isPresent()) {
                    return elseReturn;
                }
            }
        } else if (expression instanceof PearWhileDeclare whileDeclare) {
            while (evaluateBooleanValue(whileDeclare.condition(), localContext)) {
                Optional<PearValue<?>> whileReturn = executeInstructions(whileDeclare.whileBody(), localContext.copy());
                if (whileReturn.isPresent()) {
                    return whileReturn;
                }
            }
        } else if (expression instanceof PearDoWhileDeclare doWhileDeclare) {
            do {
                Optional<PearValue<?>> doWhileReturn = executeInstructions(doWhileDeclare.doBody(), localContext.copy());
                if (doWhileReturn.isPresent()) {
                    return doWhileReturn;
                }
            } while (evaluateBooleanValue(doWhileDeclare.condition(), localContext));
        } else if (expression instanceof PearForDeclare forDeclare) {
            PearContext forContext = localContext.copy();
            if (forDeclare.init() != null) {
                executeInstructions(Collections.singletonList(forDeclare.init()), forContext);
            }

            while (forDeclare.condition() == null || evaluateBooleanValue(forDeclare.condition(), forContext)) {
                Optional<PearValue<?>> forReturn = executeInstructions(forDeclare.forBody(), forContext.copy());
                if (forReturn.isPresent()) {
                    return forReturn;
                }

                if (forDeclare.increment() != null) {
                    executeInstructions(Collections.singletonList(forDeclare.increment()), forContext);
                }
            }
        } else if (expression instanceof PearVariableAssign variableAssign) {
            PearVariableState<?> variableContext = localContext.variableByName(variableAssign.name());
            variableContext.value(evaluateValue(variableAssign.value(), localContext));
        } else if (expression instanceof PearInvokeDeclare invokeDeclare) {
            invokeDeclare.invoke(localContext);
        } else {
            throw new RuntimeException("Unknown instruction " + expression);
        }

        return Optional.empty();
    }

    private boolean evaluateBooleanValue(PearValueExpression valueExpression, PearContext localContext) {
        PearValue<?> value = evaluateValue(valueExpression, localContext);
        if (value.isBoolean()) {
            return value.asBoolean();
        } else {
            throw new IllegalStateException("Expected boolean returnValue, got " + value);
        }
    }

    private PearValue<?> evaluateValue(PearValueExpression valueExpression, PearContext localContext) {
        return valueExpression.evaluate(localContext);
    }
}
