package imageviewer.architecture.control;

public interface Converter<A, B> {
    B from(A value);
}
