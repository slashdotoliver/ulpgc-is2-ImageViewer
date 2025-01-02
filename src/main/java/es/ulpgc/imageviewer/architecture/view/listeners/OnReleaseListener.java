package es.ulpgc.imageviewer.architecture.view.listeners;

public interface OnReleaseListener {
    OnReleaseListener None = (_, _) -> { };

    void offset(int value, int width);
}
