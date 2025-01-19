package es.ulpgc.imageviewer.apps.swing.view.frames;

import es.ulpgc.imageviewer.architecture.commands.CommandRegistry;
import es.ulpgc.imageviewer.architecture.view.displays.ErrorDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static es.ulpgc.imageviewer.architecture.commands.CommandName.OpenImageFolder;

public class SwingMainFrame extends JFrame {

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
        MenuItem openItem = new MenuItem("Open Folder...", new MenuShortcut(KeyEvent.VK_O));
        openItem.addActionListener(
                _ -> CommandRegistry.getInstance().get(OpenImageFolder).execute()
        );
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
