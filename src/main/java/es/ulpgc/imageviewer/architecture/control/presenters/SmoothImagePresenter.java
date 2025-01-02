package es.ulpgc.imageviewer.architecture.control.presenters;

import es.ulpgc.imageviewer.architecture.control.io.ImageLoader;
import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.view.SmoothImageDisplay;
import es.ulpgc.imageviewer.architecture.view.listeners.OnDraggingListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnReleaseListener;

public class SmoothImagePresenter implements ImagePresenter {

    private enum OnReleaseAction {
        ShowPreviousImage, ShowNextImage, KeepCurrentImage
    }

    private final SmoothImageDisplay imageDisplay;
    private Image currentImage = Image.None;
    private boolean dragging = false;
    private int initialOffset = 0;

    public SmoothImagePresenter(SmoothImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    @Override
    public void showUsing(ImageLoader loader) {
        imageDisplay.reset();
        setListeners();
        show(loader.load());
    }

    private void setListeners() {
        imageDisplay.setPreviousImageButtonListener(this::showPreviousImage);
        imageDisplay.setNextImageButtonListener(this::showNextImage);
        imageDisplay.setReleaseListener(onRelease());
        imageDisplay.setDraggingListener(onDragging());
    }

    private void show(Image image) {
        imageDisplay.show(currentImage = image);
    }

    private void showPreviousImage() {
        show(currentImage.previous());
    }

    private void showNextImage() {
        show(currentImage.next());
    }

    private OnReleaseListener onRelease() {
        return (offset, width) -> {
            unsetDragging();
            switch (nextAction(relativeOffsetOf(offset), width)) {
                case ShowPreviousImage -> showPreviousImage();
                case ShowNextImage -> showNextImage();
                case KeepCurrentImage -> imageDisplay.setGlobalOffset(0);
            }
        };
    }

    private int relativeOffsetOf(int offset) {
        return offset - initialOffset;
    }

    private boolean shouldShowPrevious(int relativeOffset, int width) {
        return relativeOffset > width / 4;
    }

    private boolean shouldShowNext(int relativeOffset, int width) {
        return relativeOffset < -width / 4;
    }

    private OnReleaseAction nextAction(int relativeOffset, int width) {
        if (shouldShowPrevious(relativeOffset, width)) return OnReleaseAction.ShowPreviousImage;
        if (shouldShowNext(relativeOffset, width)) return OnReleaseAction.ShowNextImage;
        return OnReleaseAction.KeepCurrentImage;
    }

    private OnDraggingListener onDragging() {
        return offset -> {
            setDragging(offset);
            imageDisplay.setGlobalOffset(relativeOffsetOf(offset));
        };
    }

    private void setDragging(int offset) {
        if (dragging) return;
        dragging = true;
        initialOffset = offset;
    }

    private void unsetDragging() {
        dragging = false;
        initialOffset = 0;
    }
}
