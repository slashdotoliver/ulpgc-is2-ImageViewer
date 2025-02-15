package es.ulpgc.imageviewer.apps.swing.data;

import es.ulpgc.imageviewer.architecture.data.Cache;
import es.ulpgc.imageviewer.architecture.model.CachedConverter;
import es.ulpgc.imageviewer.architecture.model.Converter;
import es.ulpgc.imageviewer.architecture.model.entities.Image;

import java.util.Optional;

public class SwingImageCachedConverter implements CachedConverter<Image, java.awt.Image> {

    private final Cache<Image, java.awt.Image> cache;
    private final Converter<Image, java.awt.Image> converter;

    public SwingImageCachedConverter(Cache<Image, java.awt.Image> cache, Converter<Image, java.awt.Image> converter) {
        this.cache = cache;
        this.converter = converter;
    }

    @Override
    public Optional<java.awt.Image> tryGetConverted(Image image) {
        return cache.has(image) ? cache.get(image) : tryPut(image);
    }

    private Optional<java.awt.Image> tryPut(Image image) {
        return tryConvert(image).map(i -> {
            cache.put(image, i);
            return i;
        });
    }

    private Optional<java.awt.Image> tryConvert(Image image) {
        try {
            return Optional.ofNullable(converter.from(image));
        } catch (Converter.ConversionException ignored) { }
        return Optional.empty();
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}
