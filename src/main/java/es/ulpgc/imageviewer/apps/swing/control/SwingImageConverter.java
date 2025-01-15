package es.ulpgc.imageviewer.apps.swing.control;

import es.ulpgc.imageviewer.architecture.control.Converter;
import es.ulpgc.imageviewer.architecture.io.Deserializer;
import es.ulpgc.imageviewer.architecture.model.Image;

import java.io.IOException;

public class SwingImageConverter implements Converter<Image, java.awt.Image> {

    private final Deserializer<byte[], java.awt.Image> deserializer;

    public SwingImageConverter(Deserializer<byte[], java.awt.Image> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public java.awt.Image from(Image image) throws ConversionException {
        try {
            return deserializer.deserialize(image.content()
                    .orElseThrow(() -> new ConversionException(
                            "Failed conversion: Converting an image without content"
                    ))
            );
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }
}
