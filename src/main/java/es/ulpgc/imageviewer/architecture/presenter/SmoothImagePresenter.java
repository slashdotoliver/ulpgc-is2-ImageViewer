package es.ulpgc.imageviewer.architecture.presenter;

import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.displays.SmoothImageDisplay;
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
    public void show(Image image) {
        imageDisplay.reset();
        setListeners();
        display(image);
    }

    @Override
    public void showPrevious() {
        display(currentImage.previous());
    }

    @Override
    public void showNext() {
        display(currentImage.next());
    }

    private void display(Image image) {
        imageDisplay.show(currentImage = image);
    }

    private void setListeners() {
        imageDisplay.setReleaseListener(onRelease());
        imageDisplay.setDraggingListener(onDragging());
    }

    private OnReleaseListener onRelease() {
        return (offset, width) -> {
            switch (nextAction(relativeOffsetOf(offset), width)) {
                case ShowPreviousImage -> showPrevious();
                case ShowNextImage -> showNext();
                case KeepCurrentImage -> imageDisplay.setGlobalOffset(0);
            }
            unsetDragging();
        };
    }

    private int relativeOffsetOf(int offset) {
        return offset - initialOffset;
    }

    private static boolean shouldShowPrevious(int relativeOffset, int width) {
        return relativeOffset > width / 4;
    }

    private static boolean shouldShowNext(int relativeOffset, int width) {
        return relativeOffset < -width / 4;
    }

    private static OnReleaseAction nextAction(int relativeOffset, int width) {
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
