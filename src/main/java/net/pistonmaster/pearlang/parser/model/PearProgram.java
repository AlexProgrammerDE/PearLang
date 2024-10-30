package net.pistonmaster.pearlang.parser.model;

import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;

import java.util.List;

public record PearProgram(List<PearCodeExpression> expressions) {
}
