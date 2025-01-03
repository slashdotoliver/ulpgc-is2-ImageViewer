package es.ulpgc.imageviewer.architecture.view;

import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.view.listeners.OnClickListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnDraggingListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnReleaseListener;

public interface SmoothImageDisplay {
    void show(Image image);

    void setNextImageButtonListener(OnClickListener listener);

    void setPreviousImageButtonListener(OnClickListener listener);

    void setDraggingListener(OnDraggingListener listener);

    void setReleaseListener(OnReleaseListener listener);

    void reset();

    void setGlobalOffset(int value);
}
