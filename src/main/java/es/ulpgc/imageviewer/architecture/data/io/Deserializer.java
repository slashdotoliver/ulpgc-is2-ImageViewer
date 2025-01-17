package es.ulpgc.imageviewer.architecture.data.io;

import java.io.IOException;

public interface Deserializer<S, T> {
    T deserialize(S input) throws IOException;
}
