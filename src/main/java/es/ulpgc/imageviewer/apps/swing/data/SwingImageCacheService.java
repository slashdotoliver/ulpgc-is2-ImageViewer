package es.ulpgc.imageviewer.apps.swing.data;

import es.ulpgc.imageviewer.apps.swing.data.io.SwingImageDeserializer;
import es.ulpgc.imageviewer.apps.swing.model.SwingImageConverter;
import es.ulpgc.imageviewer.architecture.data.Cache;
import es.ulpgc.imageviewer.architecture.model.Converter;
import es.ulpgc.imageviewer.architecture.model.entities.Image;

import java.util.Optional;

public class SwingImageCacheService {

    private final Cache<Image, java.awt.Image> cache = new SwingImageCache<String>().withKeyMapping(Image::name);
    private final Converter<Image, java.awt.Image> converter = new SwingImageConverter(new SwingImageDeserializer());

    public Optional<java.awt.Image> tryGet(Image key) {
        return cache.has(key)
                ? cache.get(key)
                : tryPutImage(key);
    }

    private Optional<java.awt.Image> tryPutImage(Image image) {
        try {
            return cache.put(image, converter.from(image));
        } catch (Converter.ConversionException e) {
            return Optional.empty();
        }
    }

    public void clear() {
        cache.clear();
    }
}
