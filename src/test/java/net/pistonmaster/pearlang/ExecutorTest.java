package net.pistonmaster.pearlang;

import net.pistonmaster.pearlang.executor.PearExecutor;
import net.pistonmaster.pearlang.parser.PearParser;
import net.pistonmaster.pearlang.parser.model.PearProgram;
import net.pistonmaster.pearlang.reader.PearReader;
import net.pistonmaster.pearlang.reader.PearTokenAndData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.simple.SimpleLogger;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecutorTest {
    @BeforeAll
    public static void init() {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    }

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
                } while test == "test4";
                
                while test == "test5" {
                  test = "test6";
                }
                
                for i := 0; i < 3; i++ {
                  test = "test7";
                  println(test);
                }
                
                lambdaTest := fn (a, b) {
                  return a + b;
                };
                
                println(lambdaTest(1, 2));
                
                return test;
                """);
        List<PearTokenAndData> tokens = pearReader.readTokens();
        // System.out.println(TestGson.GSON.toJson(tokens));
        PearParser pearLang = new PearParser(tokens);
        PearProgram program = pearLang.readProgram();
        PearExecutor executor = new PearExecutor(program);

        Optional<Object> result = executor.execute();
        assertTrue(result.isPresent());
        assertEquals("test7", result.get());
        System.out.println(result.get());
    }

    @Test
    public void calculatePi() {
        PearReader pearReader = new PearReader("""
                fn calculatePi(n) {
                  result := 0;
                  for i := 1; i < n; i++ {
                    if i % 2 == 0 {
                      result += -1.0 / (2 * i - 1);
                    } else {
                      result += 1.0 / (2 * i - 1);
                    }
                  }
                  return result * 4;
                }
                
                return calculatePi(1000);
                """);
        List<PearTokenAndData> tokens = pearReader.readTokens();
        // System.out.println(TestGson.GSON.toJson(tokens));
        PearParser pearLang = new PearParser(tokens);
        PearProgram program = pearLang.readProgram();
        PearExecutor executor = new PearExecutor(program);

        Optional<Object> result = executor.execute();
        assertTrue(result.isPresent());
        System.out.println(result.get());
    }

    @Test
    public void calculateNumber() {
        PearReader pearReader = new PearReader("""
                fn calculateNumber() {
                  return 0.1 + 0.2;
                }
                
                return calculateNumber();
                """);
        List<PearTokenAndData> tokens = pearReader.readTokens();
        // System.out.println(TestGson.GSON.toJson(tokens));
        PearParser pearLang = new PearParser(tokens);
        PearProgram program = pearLang.readProgram();
        PearExecutor executor = new PearExecutor(program);

        Optional<Object> result = executor.execute();
        assertTrue(result.isPresent());
        System.out.println(result.get());
    }

    @Test
    public void setVariable() {
        PearReader pearReader = new PearReader("""
                fn lambdaTest(a, b) {
                  return a + b;
                }
               
                lambdaTest = lambdaTest(1, 2);
               
                return lambdaTest;
                """);
        List<PearTokenAndData> tokens = pearReader.readTokens();
        // System.out.println(TestGson.GSON.toJson(tokens));
        PearParser pearLang = new PearParser(tokens);
        PearProgram program = pearLang.readProgram();
        PearExecutor executor = new PearExecutor(program);

        Optional<Object> result = executor.execute();
        assertTrue(result.isPresent());
        System.out.println(result.get());
    }
}
