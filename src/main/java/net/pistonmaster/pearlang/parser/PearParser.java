package net.pistonmaster.pearlang.parser;

import lombok.RequiredArgsConstructor;
import net.pistonmaster.pearlang.parser.model.*;
import net.pistonmaster.pearlang.parser.model.instructions.PearCodeExpression;
import net.pistonmaster.pearlang.parser.model.instructions.PearInvokeDeclare;
import net.pistonmaster.pearlang.parser.model.instructions.PearReturn;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;
import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionParameter;
import net.pistonmaster.pearlang.parser.model.instructions.logic.*;
import net.pistonmaster.pearlang.parser.model.instructions.var.PearVariableAssign;
import net.pistonmaster.pearlang.parser.model.instructions.var.PearVariableDeclare;
import net.pistonmaster.pearlang.parser.model.value.*;
import net.pistonmaster.pearlang.reader.PearToken;
import net.pistonmaster.pearlang.reader.PearTokenAndData;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class PearParser {
    private final List<PearTokenAndData> tokens;
    private final Logger logger = LoggerFactory.getLogger(PearParser.class);
    private int cursor = 0;

    public PearTokenAndData read() {
        return tokens.get(cursor++);
    }

    public PearTokenAndData readToken(PearToken token) {
        PearTokenAndData data = read();
        if (data.token() != token) {
            throw new RuntimeException("Expected " + token + " but got " + data.token());
        }
        return data;
    }

    public PearTokenAndData peek() {
        return tokens.get(cursor);
    }

    public boolean hasNext() {
        return cursor < tokens.size();
    }

    public PearProgram readProgram() {
        return new PearProgram(readFunctionBody(true));
    }

    private List<PearCodeExpression> readFunctionBody(boolean isMain) {
        List<PearCodeExpression> expressions = new ArrayList<>();
        while (hasNext()) {
            PearTokenAndData data = peek();
            if (data.token() == PearToken.CLOSE_CURLY_BRACKET && !isMain) {
                break;
            }
            expressions.add(readFunctionBodyInstruction());
        }

        return Collections.unmodifiableList(expressions);
    }

    private PearCodeExpression readFunctionBodyInstruction() {
        PearTokenAndData data = peek();
        logger.debug(data.toString());
        if (data.token() == PearToken.FN) {
            return readFunction();
        } else if (data.token() == PearToken.VAR) {
            return readVarDeclaration();
        } else if (data.token() == PearToken.RETURN) {
            return readReturn();
        } else if (data.token() == PearToken.IF) {
            return readIf();
        } else if (data.token() == PearToken.WHILE) {
            return readWhile();
        } else if (data.token() == PearToken.DO) {
            return readDoWhile();
        } else if (data.token() == PearToken.FOR) {
            return readFor();
        } else if (data.token() == PearToken.ID) {
            PearExpression expression = readIdExpression();
            readToken(PearToken.SEMICOLON);
            if (expression instanceof PearCodeExpression codeExpression) {
                return codeExpression;
            } else {
                throw new RuntimeException("Unexpected expression " + expression);
            }
        } else {
            throw new RuntimeException("Unexpected token " + data.token());
        }
    }

    private PearCodeExpression readFor() {
        readToken(PearToken.FOR);
        readToken(PearToken.OPEN_ROUND_BRACKET);
        PearCodeExpression init = null;
        PearTokenAndData data = peek();
        if (data.token() != PearToken.SEMICOLON) {
            init = readVarDeclaration();
        } else {
            readToken(PearToken.SEMICOLON);
        }
        PearValueExpression condition = null;
        PearTokenAndData conditionData = peek();
        if (conditionData.token() != PearToken.SEMICOLON) {
            condition = readBinaryValueExpression();
        }
        readToken(PearToken.SEMICOLON);
        PearCodeExpression increment = null;
        PearTokenAndData incrementData = peek();
        if (incrementData.token() != PearToken.CLOSE_ROUND_BRACKET) {
            PearExpression incrementExpression = readIdExpression();

            if (incrementExpression instanceof PearCodeExpression) {
                increment = (PearCodeExpression) incrementExpression;
            } else {
                throw new RuntimeException("Unexpected expression " + incrementExpression);
            }
        }
        readToken(PearToken.CLOSE_ROUND_BRACKET);
        readToken(PearToken.OPEN_CURLY_BRACKET);
        List<PearCodeExpression> forBody = readFunctionBody(false);
        readToken(PearToken.CLOSE_CURLY_BRACKET);
        return new PearForDeclare(init, condition, increment, forBody);
    }

    private PearCodeExpression readDoWhile() {
        readToken(PearToken.DO);
        readToken(PearToken.OPEN_CURLY_BRACKET);
        List<PearCodeExpression> doBody = readFunctionBody(false);
        readToken(PearToken.CLOSE_CURLY_BRACKET);
        readToken(PearToken.WHILE);
        readToken(PearToken.OPEN_ROUND_BRACKET);
        PearValueExpression condition = readBinaryValueExpression();
        readToken(PearToken.CLOSE_ROUND_BRACKET);
        readToken(PearToken.SEMICOLON);
        return new PearDoWhileDeclare(condition, doBody);
    }

    private PearCodeExpression readWhile() {
        readToken(PearToken.WHILE);
        readToken(PearToken.OPEN_ROUND_BRACKET);
        PearValueExpression condition = readBinaryValueExpression();
        readToken(PearToken.CLOSE_ROUND_BRACKET);
        readToken(PearToken.OPEN_CURLY_BRACKET);
        List<PearCodeExpression> whileBody = readFunctionBody(false);
        readToken(PearToken.CLOSE_CURLY_BRACKET);
        return new PearWhileDeclare(condition, whileBody);
    }

    private PearCodeExpression readIf() {
        readToken(PearToken.IF);
        readToken(PearToken.OPEN_ROUND_BRACKET);
        PearValueExpression condition = readBinaryValueExpression();
        readToken(PearToken.CLOSE_ROUND_BRACKET);
        readToken(PearToken.OPEN_CURLY_BRACKET);
        List<PearCodeExpression> ifBody = readFunctionBody(false);
        readToken(PearToken.CLOSE_CURLY_BRACKET);
        PearTokenAndData next = peek();
        if (next.token() == PearToken.ELSE) {
            readToken(PearToken.ELSE);
            PearTokenAndData next2 = peek();
            if (next2.token() == PearToken.IF) {
                return new PearIfElseDeclare(condition, ifBody, Collections.singletonList(readIf()));
            } else {
                readToken(PearToken.OPEN_CURLY_BRACKET);
                List<PearCodeExpression> elseBody = readFunctionBody(false);
                readToken(PearToken.CLOSE_CURLY_BRACKET);
                return new PearIfElseDeclare(condition, ifBody, elseBody);
            }
        }
        return new PearIfDeclare(condition, ifBody);
    }

    private PearReturn readReturn() {
        readToken(PearToken.RETURN);
        PearValueExpression value = null;
        PearTokenAndData next = peek();
        if (next.token() != PearToken.SEMICOLON) {
            value = readBinaryValueExpression();
        }
        readToken(PearToken.SEMICOLON);
        return new PearReturn(value);
    }

    private PearFunctionDeclare readFunction() {
        readToken(PearToken.FN);
        PearTokenAndData name = readToken(PearToken.ID);
        readToken(PearToken.OPEN_ROUND_BRACKET);
        List<PearFunctionParameter> parameters = new ArrayList<>();
        PearTokenAndData nextParam = peek();
        while (nextParam.token() != PearToken.CLOSE_ROUND_BRACKET) {
            PearTokenAndData parameterName = readToken(PearToken.ID);
            readToken(PearToken.COLON);
            PearTokenAndData parameterType = readToken(PearToken.ID);
            parameters.add(new PearFunctionParameter(parameterName.data(), new PearType(parameterType.data())));
            nextParam = peek();
            if (nextParam.token() == PearToken.COMMA) {
                readToken(PearToken.COMMA);
            }
        }
        readToken(PearToken.CLOSE_ROUND_BRACKET);
        PearTokenAndData nextReturnType = peek();
        PearType returnType = null;
        if (nextReturnType.token() == PearToken.COLON) {
            readToken(PearToken.COLON);
            PearTokenAndData returnTypeToken = readToken(PearToken.ID);
            returnType = new PearType(returnTypeToken.data());
        }
        readToken(PearToken.OPEN_CURLY_BRACKET);
        List<PearCodeExpression> body = readFunctionBody(false);
        readToken(PearToken.CLOSE_CURLY_BRACKET);

        return new PearFunctionDeclare(name.data(), returnType, Collections.unmodifiableList(parameters), body);
    }

    private PearVariableDeclare readVarDeclaration() {
        readToken(PearToken.VAR);
        PearTokenAndData name = readToken(PearToken.ID);
        PearType type = null;
        if (peek().token() == PearToken.COLON) {
            readToken(PearToken.COLON);
            type = new PearType(readToken(PearToken.ID).data());
        }

        PearValueExpression value = null;
        PearTokenAndData next = peek();
        if (next.token() == PearToken.ASSIGN) {
            readToken(PearToken.ASSIGN);
            value = readBinaryValueExpression();
        }

        readToken(PearToken.SEMICOLON);
        return new PearVariableDeclare(name.data(), type, value);
    }

    private PearExpression readIdExpression() {
        PearTokenAndData name = readToken(PearToken.ID);
        PearTokenAndData next = peek();
        switch (next.token()) {
            case ASSIGN, ASSIGN_PLUS, ASSIGN_MINUS, ASSIGN_DIVIDE, ASSIGN_MULTIPLY -> {
                read();
                PearValueExpression value = readBinaryValueExpression();

                if (next.token() == PearToken.ASSIGN) {
                    return new PearVariableAssign(name.data(), value);
                } else {
                    return new PearVariableAssign(name.data(), new PearBinaryExpression(new PearVariableReference(name.data()), switch (next.token()) {
                        case ASSIGN_PLUS -> PearBinaryOperator.PLUS;
                        case ASSIGN_MINUS -> PearBinaryOperator.MINUS;
                        case ASSIGN_DIVIDE -> PearBinaryOperator.DIVIDE;
                        case ASSIGN_MULTIPLY -> PearBinaryOperator.MULTIPLY;
                        default -> throw new IllegalStateException("Unexpected value: " + next.token());
                    }, value));
                }
            }
        }

        if (next.token() == PearToken.OPEN_ROUND_BRACKET) {
            readToken(PearToken.OPEN_ROUND_BRACKET);
            List<PearValueExpression> parameters = new ArrayList<>();
            PearTokenAndData nextParam = peek();
            while (nextParam.token() != PearToken.CLOSE_ROUND_BRACKET) {
                parameters.add(readBinaryValueExpression());
                nextParam = peek();
                if (nextParam.token() == PearToken.COMMA) {
                    readToken(PearToken.COMMA);
                }
            }
            readToken(PearToken.CLOSE_ROUND_BRACKET);
            return new PearInvokeDeclare(name.data(), Collections.unmodifiableList(parameters));
        }

        // TODO: Assign in theory not needed
        if (next.token() == PearToken.INCREMENT) {
            read();
            return new PearVariableAssign(name.data(),
                    new PearUnaryExpression(new PearVariableReference(name.data()), PearUnaryOperator.INCREMENT_PRE));
        } else if (next.token() == PearToken.DECREMENT) {
            read();
            return new PearVariableAssign(name.data(),
                    new PearUnaryExpression(new PearVariableReference(name.data()), PearUnaryOperator.DECREMENT_PRE));
        } else {
            return new PearVariableReference(name.data());
        }
    }

    private PearValueExpression readBinaryValueExpression() {
        PearValueExpression expression = readUnaryValueExpression();
        PearTokenAndData next = peek();
        while (next.token() == PearToken.PLUS || next.token() == PearToken.MINUS
                || next.token() == PearToken.MULTIPLY || next.token() == PearToken.DIVIDE || next.token() == PearToken.MODULO
                || next.token() == PearToken.EQUALS || next.token() == PearToken.NOT_EQUALS
                || next.token() == PearToken.GREATER_THAN || next.token() == PearToken.LESS_THAN
                || next.token() == PearToken.GREATER_THAN_OR_EQUAL || next.token() == PearToken.LESS_THAN_OR_EQUAL
                || next.token() == PearToken.AND || next.token() == PearToken.OR) {
            PearTokenAndData operator = read();
            PearValueExpression right = readUnaryValueExpression();
            expression = new PearBinaryExpression(expression, PearBinaryOperator.fromToken(operator.token()), right);
            next = peek();
        }

        return expression;
    }

    private PearValueExpression readUnaryValueExpression() {
        PearTokenAndData data = peek();
        logger.debug("Reading returnValue expression " + data);
        switch (data.token()) {
            case STRING -> {
                read();
                return new PearFixedStringExpression(data.data());
            }
            case NUMBER -> {
                read();
                return new PearFixedNumberExpression(NumberUtils.createNumber(data.data()));
            }
            case TRUE -> {
                read();
                return new PearFixedBooleanExpression(true);
            }
            case FALSE -> {
                read();
                return new PearFixedBooleanExpression(false);
            }
            case NULL -> {
                read();
                return new PearFixedNullExpression();
            }
            case ID -> {
                PearExpression idExpression = readIdExpression();
                if (idExpression instanceof PearValueExpression valueExpression) {
                    PearTokenAndData next = peek();
                    if (next.token() == PearToken.INCREMENT) {
                        read();
                        return new PearUnaryExpression(valueExpression, PearUnaryOperator.INCREMENT_POST);
                    } else if (next.token() == PearToken.DECREMENT) {
                        read();
                        return new PearUnaryExpression(valueExpression, PearUnaryOperator.DECREMENT_POST);
                    }

                    return valueExpression;
                } else {
                    throw new RuntimeException("Unexpected expression " + idExpression);
                }
            }
            case OPEN_ROUND_BRACKET -> {
                readToken(PearToken.OPEN_ROUND_BRACKET);
                PearValueExpression expression = readBinaryValueExpression();
                readToken(PearToken.CLOSE_ROUND_BRACKET);
                return expression;
            }
            case INCREMENT -> {
                read();
                PearTokenAndData tokenAndData = readToken(PearToken.ID);
                return new PearUnaryExpression(new PearVariableReference(tokenAndData.data()), PearUnaryOperator.INCREMENT_PRE);
            }
            case DECREMENT -> {
                read();
                PearTokenAndData tokenAndData = readToken(PearToken.ID);
                return new PearUnaryExpression(new PearVariableReference(tokenAndData.data()), PearUnaryOperator.DECREMENT_PRE);
            }
            case NOT -> {
                read();
                return new PearUnaryExpression(readUnaryValueExpression(), PearUnaryOperator.NOT);
            }
            case MINUS -> {
                read();
                return new PearUnaryExpression(readUnaryValueExpression(), PearUnaryOperator.MINUS);
            }
            case PLUS -> {
                read();
                return new PearUnaryExpression(readUnaryValueExpression(), PearUnaryOperator.PLUS);
            }
        }

        throw new RuntimeException("Unexpected token " + data.token());
    }
}
