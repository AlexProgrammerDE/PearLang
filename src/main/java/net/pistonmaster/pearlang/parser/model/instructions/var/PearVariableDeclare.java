package net.pistonmaster.pearlang.parser.model.instructions.var;

import net.pistonmaster.pearlang.parser.model.PearType;
import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

public record PearVariableDeclare(String name, PearType type, PearValueExpression value) implements PearCodeExpression {
}
