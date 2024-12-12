package imageviewer.architecture.view;

import imageviewer.architecture.model.Image;

public interface ImageDisplay {
    void show(Image image);

    void setNextImageButtonListener(OnClickListener listener);

    void setPreviousImageButtonListener(OnClickListener listener);
}
