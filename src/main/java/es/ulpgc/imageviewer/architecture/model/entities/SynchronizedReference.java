package es.ulpgc.imageviewer.architecture.model.entities;

import java.util.function.Consumer;

public class SynchronizedReference<V> {

    private V value;

    public SynchronizedReference(V initialValue) {
        value = initialValue;
    }

    public synchronized void set(V newValue) {
        value = newValue;
    }

    public synchronized void accessUsing(Consumer<V> consumer) {
        consumer.accept(value);
    }
}
