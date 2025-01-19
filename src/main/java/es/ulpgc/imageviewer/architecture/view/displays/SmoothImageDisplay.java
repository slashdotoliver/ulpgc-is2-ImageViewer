package es.ulpgc.imageviewer.architecture.view.displays;

import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.listeners.OnDraggingListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnReleaseListener;

public interface SmoothImageDisplay {
    void show(Image image);

    void setDraggingListener(OnDraggingListener listener);

    void setReleaseListener(OnReleaseListener listener);

    void reset();

    void setGlobalOffset(int value);
}
