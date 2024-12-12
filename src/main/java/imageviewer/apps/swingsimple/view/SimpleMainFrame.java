package imageviewer.apps.swingsimple.view;

import imageviewer.architecture.view.ImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SimpleMainFrame extends JFrame {

    private ImageDisplay imageDisplay;
    private MenuItem openItem;

    public SimpleMainFrame() throws HeadlessException {
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
        var imageDisplay = new SimpleImageDisplay();
        this.imageDisplay = imageDisplay;
        return imageDisplay;
    }

    public MenuItem getOpenFolderMenuItem() {
        return openItem;
    }

    public ImageDisplay getImageDisplay() {
        return imageDisplay;
    }
}
