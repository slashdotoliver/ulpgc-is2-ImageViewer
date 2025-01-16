package es.ulpgc.imageviewer.apps.swing.view.displays;

import es.ulpgc.imageviewer.apps.swing.data.SwingImageCachedConverter;
import es.ulpgc.imageviewer.apps.swing.view.SwingImageDrawer;
import es.ulpgc.imageviewer.architecture.model.CachedConverter;
import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.view.displays.SmoothImageDisplay;
import es.ulpgc.imageviewer.architecture.view.listeners.OnClickListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnDraggingListener;
import es.ulpgc.imageviewer.architecture.view.listeners.OnReleaseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingSmoothImageDisplay extends JPanel implements SmoothImageDisplay {

    private final SwingSimpleImageDisplay imageDisplay;
    private final AtomicInteger globalOffset = new AtomicInteger(0);
    private OnDraggingListener draggingListener = OnDraggingListener.None;
    private OnReleaseListener releasedListener = OnReleaseListener.None;

    public SwingSmoothImageDisplay() {
        setLayout(new BorderLayout());
        var converter = new SwingImageCachedConverter();
        add(imageDisplay = new SwingSimpleImageDisplay(converter), BorderLayout.CENTER);
        setImageDrawer(converter);

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

    private void setImageDrawer(CachedConverter<Image, java.awt.Image> converter) {
        imageDisplay.setImageDrawer((image, g) -> {
            drawConvertingWith(converter, image.previous(), globalOffset.get() - getWidth(), g);
            drawConvertingWith(converter, image, globalOffset.get(), g);
            drawConvertingWith(converter, image.next(), globalOffset.get() + getWidth(), g);
        });
    }

    private void drawConvertingWith(CachedConverter<Image, java.awt.Image> converter, Image image, int offset, Graphics g) {
        converter.tryGetConverted(image).ifPresent(i -> SwingImageDrawer.drawImage(i, g, offset, imageDisplay.getSize()));
    }

    @Override
    public void show(Image image) {
        globalOffset.set(0);
        imageDisplay.show(image);
    }

    @Override
    public void setPreviousImageButtonListener(OnClickListener listener) {
        imageDisplay.setPreviousImageButtonListener(listener);
    }

    @Override
    public void setNextImageButtonListener(OnClickListener listener) {
        imageDisplay.setNextImageButtonListener(listener);
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
