package es.ulpgc.imageviewer.apps.swing.view;

import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.view.SmoothImageDisplay;
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

    private final SwingSimpleImageDisplay display;
    private final AtomicInteger globalOffset = new AtomicInteger(0);
    private OnDraggingListener draggingListener = OnDraggingListener.None;
    private OnReleaseListener releasedListener = OnReleaseListener.None;

    public SwingSmoothImageDisplay() {
        setLayout(new BorderLayout());
        add(display = new SwingSimpleImageDisplay(), BorderLayout.CENTER);

        display.setImageDrawer((image, g) -> {
            display.convert(image.previous()).ifPresent(i -> display.drawImage(i, g, globalOffset.get() - getWidth()));
            display.convert(image).ifPresent(i -> display.drawImage(i, g, globalOffset.get()));
            display.convert(image.next()).ifPresent(i -> display.drawImage(i, g, globalOffset.get() + getWidth()));
        });

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

    @Override
    public void show(Image image) {
        globalOffset.set(0);
        display.show(image);
    }

    @Override
    public void setPreviousImageButtonListener(OnClickListener listener) {
        display.setPreviousImageButtonListener(listener);
    }

    @Override
    public void setNextImageButtonListener(OnClickListener listener) {
        display.setNextImageButtonListener(listener);
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
        display.reset();
    }

    @Override
    public void setGlobalOffset(int value) {
        globalOffset.set(value);
        repaint();
    }
}
