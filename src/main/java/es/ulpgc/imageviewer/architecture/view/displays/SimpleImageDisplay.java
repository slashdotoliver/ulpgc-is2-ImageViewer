package es.ulpgc.imageviewer.architecture.view.displays;

import es.ulpgc.imageviewer.architecture.model.entities.Image;

public interface SimpleImageDisplay {
    void show(Image image);

    void reset();
}
