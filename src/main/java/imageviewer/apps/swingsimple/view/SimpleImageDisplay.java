package imageviewer.apps.swingsimple.view;

import imageviewer.apps.swingsimple.control.io.SwingImageCachedConverter;
import imageviewer.architecture.control.CachedConverter;
import imageviewer.architecture.control.SynchronizedReference;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.ImageDisplay;
import imageviewer.architecture.view.OnClickListener;
import imageviewer.architecture.view.Viewport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class SimpleImageDisplay extends JPanel implements ImageDisplay {

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color BACKGROUND_COLOR = Color.darkGray;

    private final CachedConverter<Image, java.awt.Image> cachedConverter = new SwingImageCachedConverter();
    private final SynchronizedReference<Image> currentImage = new SynchronizedReference<>(Image.None);
    private final KeyListener arrowKeysListener = createArrowKeysListener();
    private final JLabel nameLabel = createNameLabel();
    private OnClickListener previousImageListener = OnClickListener.None;
    private OnClickListener nextImageListener = OnClickListener.None;

    public SimpleImageDisplay() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.black);
        setOpaque(false);

        add(createPreviousButton());
        add(Box.createGlue());
        add(createNextButton());

    private KeyListener createArrowKeysListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> previousImageListener.clickPerformed();
                    case KeyEvent.VK_RIGHT -> nextImageListener.clickPerformed();
                    default -> { }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
    }

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
        currentImage.setTo(image);
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
    public void reset() {
        cachedConverter.clearCache();
        previousImageListener = OnClickListener.None;
        nextImageListener = OnClickListener.None;
        show(Image.None);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        convertCurrentImage().ifPresent(image -> drawImage(image, g));
        super.paint(g);
    }

    private Optional<java.awt.Image> convertCurrentImage() {
        return currentImage.map(cachedConverter::convertAndCache);
    }

    private void drawImage(java.awt.Image image, Graphics g) {
        Viewport viewport = adjustedViewportTo(image);
        g.drawImage(
                image,
                viewport.x(),
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
