package es.ulpgc.imageviewer.architecture.model;

import java.util.Optional;

public interface CachedConverter<A, B> {
    Optional<B> tryGetConverted(A value);
    void clearCache();
}
