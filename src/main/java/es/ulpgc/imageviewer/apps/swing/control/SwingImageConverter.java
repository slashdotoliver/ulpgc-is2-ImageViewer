package es.ulpgc.imageviewer.apps.swing.control;

import es.ulpgc.imageviewer.architecture.control.Converter;
import es.ulpgc.imageviewer.architecture.control.io.Deserializer;
import es.ulpgc.imageviewer.architecture.model.Image;

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
