package net.pistonmaster.pearlang.reader;

public record PearTokenAndData(PearToken token, String data, int line, int column) {
}
