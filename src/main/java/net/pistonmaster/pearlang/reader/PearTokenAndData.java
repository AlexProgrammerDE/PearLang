package net.pistonmaster.pearlang.reader;

public record PearTokenAndData(PearToken token, String data, int line, int columnFrom, int columnTo) {
    public PearTokenAndData(PearToken token, String data, int line, int columnTo) {
        this(token, data, line, columnTo - data.length(), columnTo);
    }
}
