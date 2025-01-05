package es.ulpgc.imageviewer.architecture.io;

import java.io.IOException;

public interface Deserializer<Source, Target> {
    Target deserialize(Source input) throws IOException;
}
