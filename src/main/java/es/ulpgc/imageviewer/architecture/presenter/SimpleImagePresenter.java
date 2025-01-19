package es.ulpgc.imageviewer.architecture.presenter;

import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.displays.SimpleImageDisplay;

public class SimpleImagePresenter implements ImagePresenter {

    private final SimpleImageDisplay imageDisplay;
    private Image currentImage = Image.None;

    public SimpleImagePresenter(SimpleImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    @Override
    public void show(Image image) {
        imageDisplay.reset();
        imageDisplay.show(currentImage = image);
    }

    @Override
    public void showNext() {
        show(currentImage.next());
    }

    @Override
    public void showPrevious() {
        show(currentImage.previous());
    }
}
