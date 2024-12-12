package imageviewer.architecture.view;

import imageviewer.architecture.model.Image;

public interface ImageDisplay {
    void show(Image image);

    void setNextButtonListener(OnClickListener listener);

    void setPreviousButtonListener(OnClickListener listener);
}
