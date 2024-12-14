package imageviewer.apps.swingsimple.control.io;

import imageviewer.architecture.control.CachedConverter;
import imageviewer.architecture.control.io.Deserializer;
import imageviewer.architecture.model.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SwingImageCachedConverter implements CachedConverter<Image, java.awt.Image> {

    private final Deserializer<byte[], java.awt.Image> deserializer = new SwingImageDeserializer();
    private final Map<String, java.awt.Image> cache = new HashMap<>();

    @Override
    public Optional<java.awt.Image> convertAndCache(Image source) {
        if (source == Image.None) return Optional.empty();
        try {
            if (!isCached(source))
                put(source);
            return Optional.ofNullable(get(source));
        } catch (IOException ignored) { }
        return Optional.empty();
    }

    private boolean isCached(Image image) {
        return cache.containsKey(image.name());
    }

    private void put(Image image) throws IOException {
        cache.put(image.name(), deserializer.deserialize(image.content()));
    }

    private java.awt.Image get(Image image) {
        return cache.get(image.name());
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}
