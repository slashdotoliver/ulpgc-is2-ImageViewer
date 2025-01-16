package es.ulpgc.imageviewer.architecture.view.dialogs;

import java.io.File;
import java.util.Optional;

public interface FolderDialog {
    Optional<File> tryGet();
}
