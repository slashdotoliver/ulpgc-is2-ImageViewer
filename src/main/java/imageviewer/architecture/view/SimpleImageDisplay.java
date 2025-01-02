package imageviewer.architecture.view;

import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.listeners.OnClickListener;

public interface SimpleImageDisplay {
    void show(Image image);

    void setNextImageButtonListener(OnClickListener listener);

    void setPreviousImageButtonListener(OnClickListener listener);

    void reset();
}
