package imageviewer.architecture.view.listeners;

public interface OnReleaseListener {
    OnReleaseListener None = _ -> { };

    void offset(int value);
}
