package imageviewer.architecture.view;

import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.listeners.OnClickListener;
import imageviewer.architecture.view.listeners.OnDraggingListener;
import imageviewer.architecture.view.listeners.OnReleaseListener;

public interface SmoothImageDisplay {
    void show(Image image);

    void setNextImageButtonListener(OnClickListener listener);

    void setPreviousImageButtonListener(OnClickListener listener);

    void setDraggingListener(OnDraggingListener listener);

    void setReleaseListener(OnReleaseListener listener);

    void reset();

    void setGlobalOffset(int value);
}
