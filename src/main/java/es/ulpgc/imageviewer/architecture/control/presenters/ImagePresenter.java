package es.ulpgc.imageviewer.architecture.control.presenters;

import es.ulpgc.imageviewer.architecture.io.ImageLoader;

public interface ImagePresenter {
    void showUsing(ImageLoader loader);
}
