package es.ulpgc.imageviewer.architecture.data;

import java.util.Optional;

public interface Cache<K, V> {
    boolean has(K key);

    Optional<V> get(K key);

    Optional<V> put(K key, V value);

    void clear();
}
