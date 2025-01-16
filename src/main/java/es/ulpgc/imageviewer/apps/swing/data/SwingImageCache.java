package es.ulpgc.imageviewer.apps.swing.data;

import es.ulpgc.imageviewer.architecture.data.Cache;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class SwingImageCache<K> implements Cache<K, java.awt.Image> {

    private final HashMap<K, java.awt.Image> cache = new HashMap<>();

    public <I> Cache<I, java.awt.Image> withKeyMapping(Function<I, K> mapper) {
        Cache<K, java.awt.Image> imageCache = this;
        return new Cache<>() {
            @Override
            public boolean has(I key) {
                return imageCache.has(mapper.apply(key));
            }

            @Override
            public Optional<java.awt.Image> get(I key) {
                return imageCache.get(mapper.apply(key));
            }

            @Override
            public Optional<java.awt.Image> put(I key, java.awt.Image value) {
                return imageCache.put(mapper.apply(key), value);
            }

            @Override
            public void clear() {
                imageCache.clear();
            }
        };
    }

    @Override
    public boolean has(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Optional<java.awt.Image> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Optional<java.awt.Image> put(K key, java.awt.Image value) {
        cache.put(key, value);
        return Optional.ofNullable(value);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
