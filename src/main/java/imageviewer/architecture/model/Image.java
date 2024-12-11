package imageviewer.architecture.model;

import java.io.IOException;

public interface Image {
    byte[] content() throws IOException;
    Image next();
    Image previous();
}
