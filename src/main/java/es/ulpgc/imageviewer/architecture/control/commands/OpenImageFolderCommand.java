package es.ulpgc.imageviewer.architecture.control.commands;

import es.ulpgc.imageviewer.architecture.io.FolderImageLoader;
import es.ulpgc.imageviewer.architecture.control.presenters.ImagePresenter;
import es.ulpgc.imageviewer.architecture.view.ErrorDisplay;
import es.ulpgc.imageviewer.architecture.view.FolderDialog;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static es.ulpgc.imageviewer.architecture.io.FolderImageLoader.EmptyImageFolderException;

public class OpenImageFolderCommand implements Command {

    private final ImagePresenter presenter;
    private final ErrorDisplay errorDisplay;
    private final FolderDialog folderDialog;

    public OpenImageFolderCommand(
            ErrorDisplay errorDisplay,
            FolderDialog folderDialog,
            ImagePresenter presenter
    ) {
        this.errorDisplay = errorDisplay;
        this.folderDialog = folderDialog;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        Optional<File> folder = folderDialog.get();
        if (folder.isEmpty()) return;

        Optional<FolderImageLoader> loader = createLoader(folder.get());

        loader.ifPresent(presenter::showUsing);
    }

    private Optional<FolderImageLoader> createLoader(File folder) {
        try {
            return Optional.of(new FolderImageLoader(folder));
        } catch (EmptyImageFolderException | IOException e) {
            errorDisplay.show(e.getMessage());
            return Optional.empty();
        }
    }
}
