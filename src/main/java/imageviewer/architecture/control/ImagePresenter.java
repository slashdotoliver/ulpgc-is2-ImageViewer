package imageviewer.architecture.control;

import imageviewer.architecture.control.io.ImageLoader;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.ImageDisplay;

public class ImagePresenter {

    private final ImageDisplay imageDisplay;
    private Image currentImage = Image.None;

    public ImagePresenter(ImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    public void loadUsing(ImageLoader loader) {
        imageDisplay.reset();
        show(loader.load());
        imageDisplay.setPreviousImageButtonListener(() -> show(currentImage.previous()));
        imageDisplay.setNextImageButtonListener(() -> show(currentImage.next()));
    }

    private void show(Image image) {
        imageDisplay.show(currentImage = image);
    }
}
