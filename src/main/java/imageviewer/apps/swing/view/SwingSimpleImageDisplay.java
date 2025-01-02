package imageviewer.apps.swing.view;

import imageviewer.apps.swing.control.SwingImageCache;
import imageviewer.apps.swing.control.SwingImageConverter;
import imageviewer.apps.swing.control.io.SwingImageDeserializer;
import imageviewer.architecture.control.Cache;
import imageviewer.architecture.control.Converter;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.model.SynchronizedReference;
import imageviewer.architecture.view.SimpleImageDisplay;
import imageviewer.architecture.view.Viewport;
import imageviewer.architecture.view.listeners.OnClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class SwingSimpleImageDisplay extends JPanel implements SimpleImageDisplay {

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color BACKGROUND_COLOR = Color.darkGray;

    private final Cache<Image, java.awt.Image> cache = new SwingImageCache<String>().withKeyMapping(Image::name);
    private final Converter<Image, java.awt.Image> converter = new SwingImageConverter(new SwingImageDeserializer());
    private final SynchronizedReference<Image> currentImage = new SynchronizedReference<>(Image.None);
    private final KeyListener arrowKeysListener = createArrowKeysListener();
    private final JLabel nameLabel = createNameLabel();
    private OnClickListener previousImageListener = OnClickListener.None;
    private OnClickListener nextImageListener = OnClickListener.None;

    public SwingSimpleImageDisplay() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(false);

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

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
        bottomPanel.add(createNamePanel());
        return bottomPanel;
    }

    private JPanel createNamePanel() {
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
        cache.clear();
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
        return currentImage.map(
                image -> cache.has(image)
                        ? cache.get(image)
                        : cache.put(image, converter.from(image))
        );
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
