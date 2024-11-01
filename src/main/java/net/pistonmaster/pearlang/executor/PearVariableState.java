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
        this.value = (PearValue<T>) value;
    }
}
