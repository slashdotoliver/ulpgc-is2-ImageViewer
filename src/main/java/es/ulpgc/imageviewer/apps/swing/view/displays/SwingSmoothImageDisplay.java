package es.ulpgc.imageviewer.apps.swing.view.displays;

import es.ulpgc.imageviewer.apps.swing.data.SwingImageCachedConverter;
import es.ulpgc.imageviewer.apps.swing.view.displays.SwingSimpleImageDisplay.ImageDrawer;
import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.displays.SmoothImageDisplay;
import es.ulpgc.imageviewer.architecture.view.listeners.OnDraggingListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnReleaseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.atomic.AtomicInteger;

import static es.ulpgc.imageviewer.apps.swing.view.SwingImageDrawer.drawImage;

public class SwingSmoothImageDisplay extends JPanel implements SmoothImageDisplay {

    private final SwingSimpleImageDisplay imageDisplay;
    private final AtomicInteger globalOffset = new AtomicInteger(0);
    private final SwingImageCachedConverter converter = new SwingImageCachedConverter();
    private OnDraggingListener draggingListener = OnDraggingListener.None;
    private OnReleaseListener releasedListener = OnReleaseListener.None;

    public SwingSmoothImageDisplay() {
        setLayout(new BorderLayout());
        add(
                imageDisplay = new SwingSimpleImageDisplay(converter).setImageDrawer(createImageDrawer()),
                BorderLayout.CENTER
        );
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                releasedListener.offset(e.getX(), getWidth());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draggingListener.offset(e.getX());
            }
        });
    }

    private ImageDrawer createImageDrawer() {
        return (currentImage, g) -> {
            draw(currentImage.previous(), globalOffset.get() - getWidth(), g);
            draw(currentImage, globalOffset.get(), g);
            draw(currentImage.next(), globalOffset.get() + getWidth(), g);
        };
    }

    private void draw(Image image, int offset, Graphics g) {
        converter.tryGetConverted(image).ifPresent(i -> drawImage(i, g, offset, imageDisplay.getSize()));
    }

    @Override
    public void show(Image image) {
        globalOffset.set(0);
        imageDisplay.show(image);
    }

    @Override
    public void setDraggingListener(OnDraggingListener listener) {
        draggingListener = listener;
    }

    @Override
    public void setReleaseListener(OnReleaseListener listener) {
        releasedListener = listener;
    }

    @Override
    public void reset() {
        draggingListener = OnDraggingListener.None;
        releasedListener = OnReleaseListener.None;
        imageDisplay.reset();
    }

    @Override
    public void setGlobalOffset(int value) {
        globalOffset.set(value);
        repaint();
    }
}
