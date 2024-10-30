package net.pistonmaster.pearlang.reader;

public record PearTokenAndData(PearToken token, String data) {
    public PearTokenAndData(PearToken token) {
        this(token, null);
    }
}
