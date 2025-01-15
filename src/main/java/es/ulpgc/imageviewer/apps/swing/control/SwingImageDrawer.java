package es.ulpgc.imageviewer.apps.swing.control;

import es.ulpgc.imageviewer.architecture.view.Viewport;

import java.awt.*;

public class SwingImageDrawer {

    public static void drawImage(java.awt.Image image, Graphics g, int offset, Dimension canvasSize) {
        Viewport viewport = adjustedViewportTo(canvasSize, image);
        g.drawImage(
                image,
                viewport.x() + offset,
                viewport.y(),
                viewport.width(),
                viewport.height(),
                null
        );
    }

    private static Viewport adjustedViewportTo(Dimension canvasSize, java.awt.Image image) {
        return Viewport.from(canvasSize.width, canvasSize.height)
                .adjustedViewportFrom(
                        image.getWidth(null),
                        image.getHeight(null)
                );
    }
}
