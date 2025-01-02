package imageviewer.apps.swing.view;

import imageviewer.apps.swing.control.SwingImageConverter;
import imageviewer.apps.swing.control.SwingImageCache;
import imageviewer.apps.swing.control.io.SwingImageDeserializer;
import imageviewer.architecture.control.Cache;
import imageviewer.architecture.control.Converter;
import imageviewer.architecture.model.SynchronizedReference;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.SmoothImageDisplay;
import imageviewer.architecture.view.Viewport;
import imageviewer.architecture.view.listeners.OnClickListener;
import imageviewer.architecture.view.listeners.OnDraggingListener;
import imageviewer.architecture.view.listeners.OnReleaseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingSmoothImageDisplay extends JPanel implements SmoothImageDisplay {

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color BACKGROUND_COLOR = Color.darkGray;

    private final Cache<Image, java.awt.Image> imageCache = new SwingImageCache<String>().withKeyMapping(Image::name);
    private final Converter<Image, java.awt.Image> imageConverter = new SwingImageConverter(new SwingImageDeserializer());
    private final SynchronizedReference<Image> currentImage = new SynchronizedReference<>(Image.None);
    private final AtomicInteger globalOffset = new AtomicInteger(0);
    private final KeyListener arrowKeysListener = createArrowKeysListener();
    private final JLabel nameLabel = createNameLabel();
    private OnClickListener previousImageListener = OnClickListener.None;
    private OnClickListener nextImageListener = OnClickListener.None;
    private OnDraggingListener draggingListener = OnDraggingListener.None;
    private OnReleaseListener releasedListener = OnReleaseListener.None;

    public SwingSmoothImageDisplay() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(false);

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

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

    private KeyListener createArrowKeysListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> previousImageListener.clickPerformed();
                    case KeyEvent.VK_RIGHT -> nextImageListener.clickPerformed();
                }
            }
        };
    }

    private JLabel createNameLabel() {
        JLabel label = new JLabel("");
        label.setForeground(Color.white);
        return label;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);

        centerPanel.add(createPreviousButton());
        centerPanel.add(Box.createGlue());
        centerPanel.add(createNextButton());
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(createImageNamePanel());
        return bottomPanel;
    }

    private JPanel createImageNamePanel() {
        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.setBackground(BACKGROUND_COLOR);
        namePanel.add(nameLabel);
        return namePanel;
    }

    private Component createPreviousButton() {
        JButton previousImageButton = new JButton("<");
        previousImageButton.setMaximumSize(BUTTON_SIZE);
        previousImageButton.setPreferredSize(BUTTON_SIZE);
        previousImageButton.addActionListener(_ -> previousImageListener.clickPerformed());
        previousImageButton.addKeyListener(arrowKeysListener);
        return previousImageButton;
    }

    private Component createNextButton() {
        JButton nextImageButton = new JButton(">");
        nextImageButton.setMaximumSize(BUTTON_SIZE);
        nextImageButton.setPreferredSize(BUTTON_SIZE);
        nextImageButton.addActionListener(_ -> nextImageListener.clickPerformed());
        nextImageButton.addKeyListener(arrowKeysListener);
        return nextImageButton;
    }

    @Override
    public void show(Image image) {
        nameLabel.setText(image.name());
        currentImage.set(image);
        globalOffset.set(0);
        repaint();
    }

    @Override
    public void setPreviousImageButtonListener(OnClickListener listener) {
        previousImageListener = listener;
    }

    @Override
    public void setNextImageButtonListener(OnClickListener listener) {
        nextImageListener = listener;
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
        imageCache.clear();
        previousImageListener = OnClickListener.None;
        nextImageListener = OnClickListener.None;
        draggingListener = OnDraggingListener.None;
        releasedListener = OnReleaseListener.None;
        show(Image.None);
    }

    @Override
    public void setGlobalOffset(int value) {
        globalOffset.set(value);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawImages(
                currentImage.map(
                        image -> Map.of(
                                ImageDisplayContext.Previous, convert(image.previous()),
                                ImageDisplayContext.Current, convert(image),
                                ImageDisplayContext.Next, convert(image.next())
                        )
                ),
                g
        );
        super.paint(g);
    }

    private Optional<java.awt.Image> convert(Image image) {
        return imageCache.has(image)
                ? imageCache.get(image)
                : imageCache.put(image, imageConverter.from(image));
    }

    private enum ImageDisplayContext {
        Current, Previous, Next
    }

    private void drawImages(Map<ImageDisplayContext, Optional<java.awt.Image>> images, Graphics g) {
        images.get(ImageDisplayContext.Previous).ifPresent(image -> drawImage(image, g, -getWidth()));
        images.get(ImageDisplayContext.Current).ifPresent(image -> drawImage(image, g, 0));
        images.get(ImageDisplayContext.Next).ifPresent(image -> drawImage(image, g, +getWidth()));
    }

    private void drawImage(java.awt.Image image, Graphics g, int localOffset) {
        Viewport viewport = adjustedViewportTo(image);
        g.drawImage(
                image,
                viewport.x() + localOffset + globalOffset.get(),
                viewport.y(),
                viewport.width(),
                viewport.height(),
                null
        );
    }

    private Viewport adjustedViewportTo(java.awt.Image image) {
        return Viewport.from(
                        this.getWidth(),
                        this.getHeight()
                )
                .adjustedViewportFrom(
                        image.getWidth(null),
                        image.getHeight(null)
                );
    }

}
