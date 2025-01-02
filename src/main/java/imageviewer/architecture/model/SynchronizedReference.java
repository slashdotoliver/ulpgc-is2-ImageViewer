package imageviewer.architecture.model;

import java.util.function.Function;

public class SynchronizedReference<V> {

    private V value;

    public SynchronizedReference(V initialValue) {
        value = initialValue;
    }

    public synchronized <O> O map(Function<V, O> mapper) {
        return mapper.apply(value);
    }

    public synchronized void set(V newValue) {
        value = newValue;
    }
}
