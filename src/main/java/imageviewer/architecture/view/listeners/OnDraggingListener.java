package imageviewer.architecture.view.listeners;

public interface OnDraggingListener {
    OnDraggingListener None = _ -> { };

    void offset(int value);
}
