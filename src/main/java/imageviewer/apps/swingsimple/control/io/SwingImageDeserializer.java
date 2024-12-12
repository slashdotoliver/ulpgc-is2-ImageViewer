package imageviewer.apps.swingsimple.control.io;

import imageviewer.architecture.control.io.Deserializer;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SwingImageDeserializer implements Deserializer<byte[], java.awt.Image> {
    @Override
    public java.awt.Image deserialize(byte[] imageBytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }
}
