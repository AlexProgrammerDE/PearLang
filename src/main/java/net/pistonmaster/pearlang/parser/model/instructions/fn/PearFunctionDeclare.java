package net.pistonmaster.pearlang.parser.model.instructions.fn;

import net.pistonmaster.pearlang.parser.model.PearType;
import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;

import java.util.List;

public record PearFunctionDeclare(String name, PearType returnType, List<PearFunctionParameter> parameters,
                                  List<PearCodeExpression> body) implements PearCodeExpression {
}
