package imageviewer.apps.swing.view;

import imageviewer.architecture.view.FolderDialog;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class SwingFolderDialog implements FolderDialog {
    @Override
    public Optional<File> get() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = fileChooser.showOpenDialog(new JFrame("Select Folder"));

        if (option == JFileChooser.APPROVE_OPTION)
            return Optional.of(fileChooser.getSelectedFile());
        return Optional.empty();
    }
}
