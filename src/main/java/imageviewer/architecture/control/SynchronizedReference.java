package imageviewer.architecture.control;

import java.util.function.Function;

public class SynchronizedReference<V> {

    private V value;

    public SynchronizedReference(V initialValue) {
        value = initialValue;
    }

    public synchronized <O> O map(Function<V, O> mapper) {
        return mapper.apply(value);
    }

    public synchronized void setTo(V newValue) {
        value = newValue;
    }
}
