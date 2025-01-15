package es.ulpgc.imageviewer.apps.swing.io;

import es.ulpgc.imageviewer.architecture.io.Deserializer;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SwingImageDeserializer implements Deserializer<byte[], java.awt.Image> {
    @Override
    public java.awt.Image deserialize(byte[] imageBytes) throws IOException {
        try {
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (NullPointerException e) {
            throw new IOException(e);
        }
    }
}
