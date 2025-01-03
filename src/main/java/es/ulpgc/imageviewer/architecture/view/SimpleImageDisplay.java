package es.ulpgc.imageviewer.architecture.view;

import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.view.listeners.OnClickListener;

public interface SimpleImageDisplay {
    void show(Image image);

    void setNextImageButtonListener(OnClickListener listener);

    void setPreviousImageButtonListener(OnClickListener listener);

    void reset();
}
