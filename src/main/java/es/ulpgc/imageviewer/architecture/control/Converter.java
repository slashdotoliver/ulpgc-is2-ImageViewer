package es.ulpgc.imageviewer.architecture.control;

public interface Converter<A, B> {
    class ConversionException extends Exception {
        public ConversionException(String message) {
            super(message);
        }

        public ConversionException(Throwable cause) {
            super(cause);
        }
    }

    B from(A value) throws ConversionException;
}
