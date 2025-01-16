package es.ulpgc.imageviewer.architecture.data.io;

import java.io.IOException;

public interface Deserializer<Source, Target> {
    Target deserialize(Source input) throws IOException;
}
