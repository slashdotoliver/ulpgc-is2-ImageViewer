package imageviewer.apps.swing.view.smooth;

import imageviewer.architecture.model.Image;

import javax.swing.*;
import java.awt.*;

public class SmoothImageDisplay extends JPanel {

    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawImages(g);
        super.paint(g);
    }

    private void drawImages(Graphics g) {
        // TODO: remove this and do it in the smooth app
        //drawImage(g, currentImage.previous(), - getWidth());
        //drawImage(g, currentImage, 0);
        //drawImage(g, currentImage.next(), getWidth());
    }

    private void drawImage(Graphics g, Image image, int offset) {
        //java.awt.Image drawnImage = from(image);
        //if (drawnImage == null) return;
        //Viewport viewport = adjustedViewportTo(drawnImage);
        //g.drawImage(
        //        drawnImage,
        //        viewport.x() + offset,
        //        viewport.y(),
        //        viewport.width(),
        //        viewport.height(),
        //        null
        //);
    }
}
