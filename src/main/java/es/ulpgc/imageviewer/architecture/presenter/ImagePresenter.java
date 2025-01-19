package es.ulpgc.imageviewer.architecture.presenter;

import es.ulpgc.imageviewer.architecture.model.entities.Image;

public interface ImagePresenter {
    void show(Image image);
    void showNext();
    void showPrevious();
}
