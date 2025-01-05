package es.ulpgc.imageviewer.architecture.control.presenters;

import es.ulpgc.imageviewer.architecture.io.ImageLoader;
import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.view.SimpleImageDisplay;

public class SimpleImagePresenter implements ImagePresenter {

    private final SimpleImageDisplay imageDisplay;
    private Image currentImage = Image.None;

    public SimpleImagePresenter(SimpleImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    @Override
    public void showUsing(ImageLoader loader) {
        imageDisplay.reset();
        show(loader.load());
        imageDisplay.setPreviousImageButtonListener(() -> show(currentImage.previous()));
        imageDisplay.setNextImageButtonListener(() -> show(currentImage.next()));
    }

    private void show(Image image) {
        imageDisplay.show(currentImage = image);
    }
}
