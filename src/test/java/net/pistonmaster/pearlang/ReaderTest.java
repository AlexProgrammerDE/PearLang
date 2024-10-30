package net.pistonmaster.pearlang;

import net.pistonmaster.pearlang.reader.PearReader;
import net.pistonmaster.pearlang.reader.PearTokenAndData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ReaderTest {
    @Test
    public void test() {
        PearReader pearReader = new PearReader("""
                fn test(parameter: String, test2: int): String {
                  return null;
                }
                """);

        List<PearTokenAndData> tokens = pearReader.readTokens();
        tokens.forEach(System.out::println);

        assertNotEquals(0, tokens.size());
        assertEquals(0, pearReader.readTokens().size());
    }
}
