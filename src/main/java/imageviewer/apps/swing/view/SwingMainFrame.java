package imageviewer.apps.swing.view;

import imageviewer.apps.swing.view.simple.SwingSimpleImageDisplay;
import imageviewer.architecture.view.ErrorDisplay;
import imageviewer.architecture.view.SimpleImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SwingMainFrame extends JFrame {

    private SimpleImageDisplay imageDisplay;
    private MenuItem openItem;

    public SwingMainFrame() throws HeadlessException {
        setTitle("Simple Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(150, 150));
        setSize(800, 800);
        setResizable(true);
        setLocationRelativeTo(null);

        createMenuBar();
        add(createImageDisplay());
    }

    private void createMenuBar() {
        MenuBar menu = new MenuBar();
        Menu file = new Menu("File");
        file.add(createOpenMenuItem());
        menu.add(file);
        setMenuBar(menu);
    }

    private MenuItem createOpenMenuItem() {
        openItem = new MenuItem("Open Folder...", new MenuShortcut(KeyEvent.VK_O));
        return openItem;
    }

    private Component createImageDisplay() {
        var imageDisplay = new SwingSimpleImageDisplay();
        this.imageDisplay = imageDisplay;
        return imageDisplay;
    }

    public MenuItem getOpenFolderMenuItem() {
        return openItem;
    }

    public SimpleImageDisplay getImageDisplay() {
        return imageDisplay;
    }

    public ErrorDisplay getErrorDisplay() {
        return message -> JOptionPane.showMessageDialog(
                null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
