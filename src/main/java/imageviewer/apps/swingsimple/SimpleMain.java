package imageviewer.apps.swingsimple;

import imageviewer.apps.swingsimple.view.SimpleMainFrame;
import imageviewer.architecture.control.ImagePresenter;
import imageviewer.architecture.control.io.FolderImageLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class SimpleMain {

    private static final Logger LOGGER = Logger.getLogger(SimpleMain.class.getSimpleName());
    private static ImagePresenter presenter;

    public static void main(String[] args) {
        changeLookAndFeel();
        var mainFrame = new SimpleMainFrame();
        try {
            presenter = new ImagePresenter(
                    mainFrame.getImageDisplay(),
                    new FolderImageLoader(new File("./images"))
            );
        } catch (IOException | FolderImageLoader.EmptyImageFolderException e) {
            LOGGER.severe(e.getMessage());
        }
        mainFrame.setVisible(true);
    }

    private static void changeLookAndFeel() {
        Optional<String> look = findFirstOccurrence(
                List.of(
                        "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                ),
                Arrays.stream(UIManager.getInstalledLookAndFeels())
                        .map(UIManager.LookAndFeelInfo::getClassName)
                        .toList()
        );
        if (look.isPresent()) {
            try {
                UIManager.setLookAndFeel(look.get());
            } catch (
                    ClassNotFoundException |
                    InstantiationException |
                    IllegalAccessException |
                    UnsupportedLookAndFeelException e
            ) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    private static Optional<String> findFirstOccurrence(List<String> preferences, List<String> options) {
        return preferences.stream()
                .filter(preference -> options.stream()
                        .anyMatch(preference::equals))
                .findFirst();
    }
}
