package net.pistonmaster.pearlang.parser.model.instructions;

import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

public record PearReturn(PearValueExpression returnValue) implements PearCodeExpression {
}
