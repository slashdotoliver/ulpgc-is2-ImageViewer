package es.ulpgc.imageviewer.architecture.control.presenters;

import es.ulpgc.imageviewer.architecture.control.io.ImageLoader;

public interface ImagePresenter {
    void showUsing(ImageLoader loader);
}
