package es.ulpgc.imageviewer.apps.swing.view;

import es.ulpgc.imageviewer.architecture.view.FolderDialog;

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
