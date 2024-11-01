package net.pistonmaster.pearlang.reader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PearToken {
    EOF, // End of File
    STRING,
    NUMBER,
    ID,

    FN(PearTokenType.KEYWORD, "fn"),
    IF(PearTokenType.KEYWORD, "if"),
    ELSE(PearTokenType.KEYWORD, "else"),
    WHILE(PearTokenType.KEYWORD, "while"),
    DO(PearTokenType.KEYWORD, "do"),
    RETURN(PearTokenType.KEYWORD, "return"),
    TRUE(PearTokenType.KEYWORD, "true"),
    FALSE(PearTokenType.KEYWORD, "false"),
    FOR(PearTokenType.KEYWORD, "for"),
    NULL(PearTokenType.KEYWORD, "null"),

    SEMICOLON(PearTokenType.SYNTAX, ";"),
    COLON(PearTokenType.SYNTAX, ":"),
    OPEN_ROUND_BRACKET(PearTokenType.SYNTAX, "("),
    CLOSE_ROUND_BRACKET(PearTokenType.SYNTAX, ")"),
    OPEN_CURLY_BRACKET(PearTokenType.SYNTAX, "{"),
    CLOSE_CURLY_BRACKET(PearTokenType.SYNTAX, "}"),
    OPEN_SQUARE_BRACKET(PearTokenType.SYNTAX, "["),
    CLOSE_SQUARE_BRACKET(PearTokenType.SYNTAX, "]"),
    COMMA(PearTokenType.SYNTAX, ","),
    ASSIGN(PearTokenType.SYNTAX, "="),
    ASSIGN_PLUS(PearTokenType.SYNTAX, "+="),
    ASSIGN_MINUS(PearTokenType.SYNTAX, "-="),
    ASSIGN_MULTIPLY(PearTokenType.SYNTAX, "*="),
    ASSIGN_DIVIDE(PearTokenType.SYNTAX, "/="),
    EQUALS(PearTokenType.SYNTAX, "=="),
    NOT_EQUALS(PearTokenType.SYNTAX, "!="),
    LESS_THAN(PearTokenType.SYNTAX, "<"),
    LESS_THAN_OR_EQUAL(PearTokenType.SYNTAX, "<="),
    GREATER_THAN(PearTokenType.SYNTAX, ">"),
    GREATER_THAN_OR_EQUAL(PearTokenType.SYNTAX, ">="),
    PLUS(PearTokenType.SYNTAX, "+"),
    MINUS(PearTokenType.SYNTAX, "-"),
    MULTIPLY(PearTokenType.SYNTAX, "*"),
    DIVIDE(PearTokenType.SYNTAX, "/"),
    MODULO(PearTokenType.SYNTAX, "%"),
    INCREMENT(PearTokenType.SYNTAX, "++"),
    DECREMENT(PearTokenType.SYNTAX, "--"),
    AND(PearTokenType.SYNTAX, "&&"),
    OR(PearTokenType.SYNTAX, "||"),
    NOT(PearTokenType.SYNTAX, "!"),
    VAR_DECLARE(PearTokenType.SYNTAX, ":=");

    private final PearTokenType type;
    private final String content;

    PearToken() {
        this.type = PearTokenType.SYNTAX;
        this.content = null;
    }

    public static List<PearToken> getSyntaxSorted() {
        List<PearToken> syntax = new ArrayList<>();

        for (PearToken token : PearToken.values()) {
            if (token.getType() == PearTokenType.SYNTAX && token.getContent() != null) {
                syntax.add(token);
            }
        }

        syntax.sort(Comparator.comparing(PearToken::getContent).reversed());

        return Collections.unmodifiableList(syntax);
    }
}
