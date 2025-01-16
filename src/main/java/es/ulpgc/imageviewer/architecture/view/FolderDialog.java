package es.ulpgc.imageviewer.architecture.view;

import java.io.File;
import java.util.Optional;

public interface FolderDialog {
    Optional<File> tryGet();
}
