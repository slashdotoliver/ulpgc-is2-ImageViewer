package es.ulpgc.imageviewer.architecture.model;

import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

public interface Image {
    Optional<byte[]> content() throws IOException;
    Image next();
    Image previous();
    String name();

    Image None = new Image() {
        @Override
        public Optional<byte[]> content() {
            return Optional.empty();
        }

        @Override
        public Image next() {
            return None;
        }

        @Override
        public Image previous() {
            return None;
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Image.class.getSimpleName() + ".None[", "]")
                    .add("name: '" + name() + "'")
                    .add("previous: '" + previous().name() + "'")
                    .add("next: '" + next().name() + "'")
                    .toString();
        }
    };
}
