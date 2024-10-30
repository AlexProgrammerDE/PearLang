package net.pistonmaster.pearlang.parser.model.instructions.logic;

import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

import java.util.List;

public record PearIfDeclare(PearValueExpression condition,
                            List<PearCodeExpression> ifBody) implements PearCodeExpression {
}
