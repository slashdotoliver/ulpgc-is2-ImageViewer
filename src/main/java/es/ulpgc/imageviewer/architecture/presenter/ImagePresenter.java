package es.ulpgc.imageviewer.architecture.presenter;

import es.ulpgc.imageviewer.architecture.data.io.loaders.ImageLoader;

public interface ImagePresenter {
    void showUsing(ImageLoader loader);
}
