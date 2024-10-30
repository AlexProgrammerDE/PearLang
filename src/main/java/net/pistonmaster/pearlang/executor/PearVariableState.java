package net.pistonmaster.pearlang.executor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PearVariableState<T> {
    private final String name;
    private PearValue<T> value;

    public String name() {
        return name;
    }

    public PearValue<T> value() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public <P> void value(PearValue<P> value) {
        if (value.clazz() != null && value.clazz() != this.value.clazz()) {
            throw new IllegalArgumentException("Cannot assign value of type " + value.clazz() + " to variable of type " + this.value.clazz());
        }

        this.value = (PearValue<T>) value;
    }
}
