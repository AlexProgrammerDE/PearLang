package net.pistonmaster.pearlang;

import net.pistonmaster.pearlang.parser.PearParser;
import net.pistonmaster.pearlang.parser.model.PearProgram;
import net.pistonmaster.pearlang.reader.PearReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParserTest {

    @Test
    public void test() {
        PearReader pearReader = new PearReader("""
                fn returnedNull(parameter, test2) {
                  return null;
                }
                test := "test";
                if test == "test" {
                  test = "test2";
                } else if test2 {
                  test = "test3";
                } else {
                  test = "test4";
                  return;
                }
                
                do {
                  test = "test5";
                } while test == "test5";
                
                while test == "test5" {
                  test = "test6";
                }
                
                for i := 0; i < 10; i++ {
                  test = "test7";
                }
                
                lambdaTest := fn (a, b) {
                  return a + b;
                };
                
                println(lambdaTest(1, 2));
                
                return test;
                """);
        PearParser pearLang = new PearParser(pearReader.readTokens());
        PearProgram program = pearLang.readProgram();

        System.out.println(program);
        System.out.println(TestGson.GSON.toJson(program));

        assertNotNull(program);
    }
}
