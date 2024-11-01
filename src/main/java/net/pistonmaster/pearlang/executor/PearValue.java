package net.pistonmaster.pearlang.executor;

import net.pistonmaster.pearlang.parser.model.instructions.fn.PearFunctionDeclare;

public record PearValue<T>(Class<T> clazz, T value) {
    public PearValue<T> copy() {
        return new PearValue<>(clazz, value);
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isFunction() {
        return value instanceof PearFunctionDeclare;
    }

    public Number asNumber() {
        return (Number) value;
    }

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public String asString() {
        return (String) value;
    }

    public PearFunctionDeclare asFunction() {
        return (PearFunctionDeclare) value;
    }
}
