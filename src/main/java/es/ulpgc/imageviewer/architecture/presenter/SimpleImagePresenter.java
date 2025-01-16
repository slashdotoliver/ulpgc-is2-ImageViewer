package es.ulpgc.imageviewer.architecture.presenter;

import es.ulpgc.imageviewer.architecture.data.io.loaders.ImageLoader;
import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.displays.SimpleImageDisplay;

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
