package net.pistonmaster.pearlang.parser.model.instructions.logic;

import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

import java.util.List;

public record PearForDeclare(PearCodeExpression init, PearValueExpression condition, PearCodeExpression increment,
                             List<PearCodeExpression> forBody) implements PearCodeExpression {
}
