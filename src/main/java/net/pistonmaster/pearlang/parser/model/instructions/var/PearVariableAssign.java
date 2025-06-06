package net.pistonmaster.pearlang.parser.model.instructions.var;

import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

public record PearVariableAssign(String name, PearValueExpression value) implements PearCodeExpression {
}
