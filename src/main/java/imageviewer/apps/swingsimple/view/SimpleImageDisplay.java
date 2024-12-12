package imageviewer.apps.swingsimple.view;

import imageviewer.apps.swingsimple.control.io.SwingImageDeserializer;
import imageviewer.architecture.control.io.Deserializer;
import imageviewer.architecture.model.Image;
import imageviewer.architecture.view.ImageDisplay;
import imageviewer.architecture.view.OnClickListener;
import imageviewer.architecture.view.Viewport;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SimpleImageDisplay extends JPanel implements ImageDisplay {

    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);

    private final Deserializer<byte[], java.awt.Image> deserializer = new SwingImageDeserializer();
    private OnClickListener previousImageListener = OnClickListener.None;
    private OnClickListener nextImageListener = OnClickListener.None;

    private Image currentImage; // TODO: create a mechanism to atomically access the current image

    public SimpleImageDisplay() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.black);
        setOpaque(false);

        add(createPreviousButton());
        add(Box.createGlue());
        add(createNextButton());
    }

    private Component createPreviousButton() {
        JButton previousImageButton = new JButton("<");
        previousImageButton.setMaximumSize(BUTTON_SIZE);
        previousImageButton.setPreferredSize(BUTTON_SIZE);
        previousImageButton.addActionListener(_ -> previousImageListener.clickPerformed());
        return previousImageButton;
    }

    private Component createNextButton() {
        JButton nextImageButton = new JButton(">");
        nextImageButton.setMaximumSize(BUTTON_SIZE);
        nextImageButton.setPreferredSize(BUTTON_SIZE);
        nextImageButton.addActionListener(_ -> nextImageListener.clickPerformed());
        return nextImageButton;
    }

    @Override
    public void show(Image image) {
        synchronized (this) {
            currentImage = image;
        }
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
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawImage(g);
        super.paint(g);
    }

    private void drawImage(Graphics g) {
        java.awt.Image image = fromCurrentImage();
        if (image == null) return;
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

    private java.awt.Image fromCurrentImage() {
        java.awt.Image image;
        synchronized (this) {
            {
                if (currentImage == null) return null;
                try {
                    image = swingImageFrom(currentImage);
                } catch (IOException ignored) {
                    return null;
                }
            }
        }
        return image;
    }

    private java.awt.Image swingImageFrom(Image image) throws IOException {
        // TODO: add cache
        // TODO: add interface method to ImageDisplay to clean the cache
        return deserializer.deserialize(image.content());
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
