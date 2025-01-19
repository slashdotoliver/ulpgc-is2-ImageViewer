package es.ulpgc.imageviewer.apps.swing.view.displays;

import es.ulpgc.imageviewer.apps.swing.utils.JButtonBuilder;
import es.ulpgc.imageviewer.apps.swing.utils.JPanelBuilder;
import es.ulpgc.imageviewer.architecture.commands.CommandName;
import es.ulpgc.imageviewer.architecture.commands.CommandRegistry;
import es.ulpgc.imageviewer.architecture.model.CachedConverter;
import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.model.entities.SynchronizedReference;
import es.ulpgc.imageviewer.architecture.view.displays.SimpleImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static es.ulpgc.imageviewer.apps.swing.view.SwingImageDrawer.drawImage;

public class SwingSimpleImageDisplay extends JPanel implements SimpleImageDisplay {

    public interface ImageDrawer {
        void draw(Image current, Graphics g);
    }

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color BACKGROUND_COLOR = Color.darkGray;

    private final CachedConverter<Image, java.awt.Image> converter;
    private final SynchronizedReference<Image> currentImage = new SynchronizedReference<>(Image.None);
    private final JLabel nameLabel = createNameLabel();
    private ImageDrawer drawer = defaultImageDrawer();

    public SwingSimpleImageDisplay(CachedConverter<Image, java.awt.Image> converter) {
        this.converter = converter;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setOpaque(false);

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private ImageDrawer defaultImageDrawer() {
        return (image, g) -> converter.tryGetConverted(image)
                .ifPresent(i -> drawImage(i, g, 0, getSize()));
    }

    public SwingSimpleImageDisplay setImageDrawer(ImageDrawer drawer) {
        this.drawer = drawer;
        return this;
    }

    private KeyListener createArrowKeysListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> CommandRegistry.getInstance()
                            .get(CommandName.PreviousImage)
                            .execute();
                    case KeyEvent.VK_RIGHT -> CommandRegistry.getInstance()
                            .get(CommandName.NextImage)
                            .execute();
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

    private JButton createPreviousButton(JButtonBuilder builder) {
        return builder
                .setActionListener(_ -> CommandRegistry.getInstance()
                        .get(CommandName.PreviousImage)
                        .execute()
                )
                .setText("<")
                .build();
    }

    private JButton createNextButton(JButtonBuilder builder) {
        return builder
                .setActionListener(_ -> CommandRegistry.getInstance()
                        .get(CommandName.NextImage)
                        .execute()
                )
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
    public void reset() {
        converter.clearCache();
        show(Image.None);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawWithImageDrawer(g);
        super.paint(g);
    }

    private void drawWithImageDrawer(Graphics g) {
        currentImage.accessUsing(image -> drawer.draw(image, g));
    }
}
