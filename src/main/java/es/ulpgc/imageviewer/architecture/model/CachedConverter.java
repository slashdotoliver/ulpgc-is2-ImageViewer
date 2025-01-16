package es.ulpgc.imageviewer.architecture.model;

import java.util.Optional;

public interface CachedConverter<K, V> {
    Optional<V> tryGetConverted(K value);
    void clearCache();
}
