package net.pistonmaster.pearlang;

import com.google.gson.Gson;
import io.gsonfire.GsonFireBuilder;
import net.pistonmaster.pearlang.parser.model.PearExpression;
import org.reflections.Reflections;

import java.util.Optional;
import java.util.Set;

public class TestGson {

    public static final Gson GSON;

    static {
        GsonFireBuilder fireBuilder = new GsonFireBuilder();

        Reflections reflections = new Reflections("net.pistonmaster.pearlang");
        Set<Class<? extends PearExpression>> classes = reflections.getSubTypesOf(PearExpression.class);

        for (Class<? extends PearExpression> clazz : classes) {
            fireBuilder.wrap(clazz, clazz.getSimpleName());
        }

        GSON = fireBuilder.createGsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Optional.class, new OptionalAdapter())
                .create();
    }

    private static class OptionalAdapter extends com.google.gson.TypeAdapter<Optional<?>> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, Optional<?> value) {
            if (value.isPresent()) {
                try {
                    out.jsonValue(GSON.toJson(value.get()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    out.nullValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public Optional<?> read(com.google.gson.stream.JsonReader in) {
            return Optional.empty();
        }
    }
}
