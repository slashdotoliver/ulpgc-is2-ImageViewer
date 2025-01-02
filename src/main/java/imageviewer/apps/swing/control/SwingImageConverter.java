package imageviewer.apps.swing.control;

import imageviewer.architecture.control.Converter;
import imageviewer.architecture.control.io.Deserializer;
import imageviewer.architecture.model.Image;

import java.io.IOException;

public class SwingImageConverter implements Converter<Image, java.awt.Image> {

    private final Deserializer<byte[], java.awt.Image> deserializer;

    public SwingImageConverter(Deserializer<byte[], java.awt.Image> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public java.awt.Image from(Image image) {
        try {
            return deserializer.deserialize(image.content());
        } catch (IOException ignored) {
            return null;
        }
    }
}
