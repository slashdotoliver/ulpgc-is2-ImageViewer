package es.ulpgc.imageviewer.apps.swing.view;

import es.ulpgc.imageviewer.architecture.view.ErrorDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SwingMainFrame extends JFrame {

    private MenuItem openItem;

    public SwingMainFrame(JPanel imageDisplayPanel, String frameTitle) throws HeadlessException {
        setTitle(frameTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(150, 150));
        setSize(800, 800);
        setResizable(true);
        setLocationRelativeTo(null);

        createMenuBar();
        add(imageDisplayPanel);
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

    public MenuItem getOpenFolderMenuItem() {
        return openItem;
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
