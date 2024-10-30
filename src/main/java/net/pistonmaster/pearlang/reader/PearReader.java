package net.pistonmaster.pearlang.reader;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PearReader {
    public static final char EOF = '\0';
    public static final char STRING = '"';
    public static final char COMMENT = '#';
    public static final Pattern IDENTIFIER_START_PATTERN = Pattern.compile("[a-zA-Z]");
    public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z0-9_]");
    public static final Set<Character> NUMBER_END_PATTERNS = Set.of('F', 'f', 'D', 'd', 'L', 'l');
    private final String pearSource;
    private final Logger logger = LoggerFactory.getLogger(PearReader.class);
    private int cursor = 0;

    public boolean hasNext() {
        return cursor < pearSource.length();
    }

    public char read() {
        if (cursor >= pearSource.length()) {
            return EOF;
        }
        return pearSource.charAt(cursor++);
    }

    public char peek() {
        if (cursor >= pearSource.length()) {
            return EOF;
        }
        return pearSource.charAt(cursor);
    }

    public void skipWhitespace() {
        while (hasNext() && Character.isWhitespace(peek())) {
            read();
        }
    }

    public PearTokenAndData readNextToken() {
        skipWhitespace();
        char c = peek();
        if (c == EOF) {
            return null;
        }

        if (c == COMMENT) {
            while (hasNext() && peek() != '\n') {
                read();
            }
            return readNextToken();
        }

        if (c == STRING) {
            return readString();
        }

        if (Character.isDigit(c)) {
            return readNumber();
        }

        if (IDENTIFIER_START_PATTERN.matcher(String.valueOf(c)).matches()) {
            return readIdentifier();
        }

        for (PearToken token : PearToken.getSyntaxSorted()) {
            int startCursor = cursor;
            StringBuilder sb = new StringBuilder();
            while (hasNext()) {
                char next = peek();
                if (next == EOF
                        || !token.getContent().contains(String.valueOf(next))
                        || sb.length() >= token.getContent().length()) {
                    break;
                }
                sb.append(read());
            }

            if (!token.getContent().contentEquals(sb)) {
                cursor = startCursor;
                continue;
            }

            logger.debug("Found token: {} {}", token, sb);
            return new PearTokenAndData(token, sb.toString());
        }

        throw new RuntimeException("Unexpected character: " + c);
    }

    private PearTokenAndData readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (hasNext()) {
            char c = peek();
            if (!IDENTIFIER_PATTERN.matcher(String.valueOf(c)).matches()) {
                break;
            }

            sb.append(read());
        }

        for (PearToken token : PearToken.values()) {
            if (token.getType() == PearTokenType.KEYWORD
                    && token.getContent() != null && token.getContent().contentEquals(sb)) {
                return new PearTokenAndData(token, token.getContent());
            }
        }

        return new PearTokenAndData(PearToken.ID, sb.toString());
    }

    private PearTokenAndData readString() {
        StringBuilder sb = new StringBuilder();
        read(); // skip first "
        while (hasNext()) {
            char c = read();
            if (c == STRING) {
                break;
            }
            sb.append(c);
        }
        return new PearTokenAndData(PearToken.STRING, sb.toString());
    }

    private PearTokenAndData readNumber() {
        StringBuilder sb = new StringBuilder();
        while (hasNext()) {
            char c = peek();
            if (!Character.isDigit(c) && c != '.' && !NUMBER_END_PATTERNS.contains(c)) {
                break;
            }
            sb.append(read());
        }

        NumberUtils.createNumber(sb.toString());

        return new PearTokenAndData(PearToken.NUMBER, sb.toString());
    }

    public List<PearTokenAndData> readTokens() {
        List<PearTokenAndData> tokens = new ArrayList<>();

        while (hasNext()) {
            PearTokenAndData token = readNextToken();
            if (token == null) {
                break;
            }

            logger.debug("Read token: " + token);
            tokens.add(token);
        }

        return tokens;
    }

}
