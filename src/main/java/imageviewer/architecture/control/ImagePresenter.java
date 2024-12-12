package imageviewer.architecture.control;

import imageviewer.architecture.control.io.ImageLoader;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.ImageDisplay;

public class ImagePresenter {

    private final ImageDisplay imageDisplay;

    private Image currentImage;

    public ImagePresenter(ImageDisplay imageDisplay, ImageLoader loader) {
        this.imageDisplay = imageDisplay;
        imageDisplay.setPreviousImageButtonListener(() -> show(currentImage.previous()));
        imageDisplay.setNextImageButtonListener(() -> show(currentImage.next()));
        show(loader.load());
    }

    private void show(Image image) {
        imageDisplay.show(this.currentImage = image);
    }
}
