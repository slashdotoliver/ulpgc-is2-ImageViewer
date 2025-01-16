package es.ulpgc.imageviewer.apps.swing.view.dialogs;

import es.ulpgc.imageviewer.architecture.view.dialogs.FolderDialog;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class SwingFolderDialog implements FolderDialog {
    @Override
    public Optional<File> tryGet() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(new JFrame("Select Folder")) == JFileChooser.APPROVE_OPTION)
            return Optional.of(fileChooser.getSelectedFile());
        return Optional.empty();
    }
}
