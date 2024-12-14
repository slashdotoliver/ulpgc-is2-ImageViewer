package imageviewer.architecture.control.presenters;

import imageviewer.architecture.control.io.ImageLoader;

public interface ImagePresenter {
    void loadUsing(ImageLoader loader);
}
