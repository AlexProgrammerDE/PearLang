package net.pistonmaster.pearlang.parser.model.instructions.logic;

import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.value.PearValueExpression;

import java.util.List;

public record PearDoWhileDeclare(PearValueExpression condition,
                                 List<PearCodeExpression> doBody) implements PearCodeExpression {
}
