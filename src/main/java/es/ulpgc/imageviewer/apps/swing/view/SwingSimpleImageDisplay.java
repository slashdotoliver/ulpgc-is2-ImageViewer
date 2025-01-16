package es.ulpgc.imageviewer.apps.swing.view;

import es.ulpgc.imageviewer.apps.swing.control.SwingImageCache;
import es.ulpgc.imageviewer.apps.swing.control.SwingImageConverter;
import es.ulpgc.imageviewer.apps.swing.control.SwingImageDrawer;
import es.ulpgc.imageviewer.apps.swing.io.SwingImageDeserializer;
import es.ulpgc.imageviewer.apps.swing.utils.JButtonBuilder;
import es.ulpgc.imageviewer.apps.swing.utils.JPanelBuilder;
import es.ulpgc.imageviewer.architecture.control.Cache;
import es.ulpgc.imageviewer.architecture.control.Converter;
import es.ulpgc.imageviewer.architecture.model.Image;
import es.ulpgc.imageviewer.architecture.model.SynchronizedReference;
import es.ulpgc.imageviewer.architecture.view.SimpleImageDisplay;
import es.ulpgc.imageviewer.architecture.view.listeners.OnClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class SwingSimpleImageDisplay extends JPanel implements SimpleImageDisplay {

    public interface ImageDrawer {
        void draw(Image current, Graphics g);
    }

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color BACKGROUND_COLOR = Color.darkGray;

    private final Cache<Image, java.awt.Image> imageCache = new SwingImageCache<String>().withKeyMapping(Image::name);
    private final Converter<Image, java.awt.Image> imageConverter = new SwingImageConverter(new SwingImageDeserializer());
    private final SynchronizedReference<Image> currentImage = new SynchronizedReference<>(Image.None);
    private final JLabel nameLabel = createNameLabel();
    private OnClickListener previousImageListener = OnClickListener.None;
    private OnClickListener nextImageListener = OnClickListener.None;
    private ImageDrawer drawer;

    public SwingSimpleImageDisplay() {
        this.drawer = (image, g) -> convert(image).ifPresent(i -> drawImage(i, g, 0));
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(false);

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    public void setImageDrawer(ImageDrawer drawer) {
        this.drawer = drawer;
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
        JButtonBuilder buttonBuilder = new JButtonBuilder()
                .setMaximumSize(BUTTON_SIZE)
                .setPreferredSize(BUTTON_SIZE)
                .setKeyListener(createArrowKeysListener());
        return JPanelBuilder.withBoxLayout(BoxLayout.X_AXIS)
                .setOpaque(false)
                .add(createPreviousButton(buttonBuilder))
                .add(Box.createGlue())
                .add(createNextButton(buttonBuilder))
                .build();
    }

    private JPanel createBottomPanel() {
        return JPanelBuilder.withFlowLayout()
                .setOpaque(false)
                .add(JPanelBuilder.withFlowLayout()
                        .setBackground(BACKGROUND_COLOR)
                        .add(nameLabel)
                        .build())
                .build();
    }

    private Component createPreviousButton(JButtonBuilder builder) {
        return builder
                .setActionListener(_ -> previousImageListener.clickPerformed())
                .setText("<")
                .build();
    }

    private Component createNextButton(JButtonBuilder builder) {
        return builder
                .setActionListener(_ -> nextImageListener.clickPerformed())
                .setText(">")
                .build();
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
        imageCache.clear();
        previousImageListener = OnClickListener.None;
        nextImageListener = OnClickListener.None;
        show(Image.None);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawImage(g);
        super.paint(g);
    }

    private void drawImage(Graphics g) {
        currentImage.accessUsing(image -> drawer.draw(image, g));
    }

    public void drawImage(java.awt.Image image, Graphics g, int offset) {
        SwingImageDrawer.drawImage(image, g, offset, getSize());
    }

    public Optional<java.awt.Image> convert(Image image) {
        return imageCache.has(image)
                ? imageCache.get(image)
                : tryPutImage(image);
    }

    private Optional<java.awt.Image> tryPutImage(Image image) {
        try {
            return imageCache.put(image, imageConverter.from(image));
        } catch (Converter.ConversionException e) {
            return Optional.empty();
        }
    }
}
