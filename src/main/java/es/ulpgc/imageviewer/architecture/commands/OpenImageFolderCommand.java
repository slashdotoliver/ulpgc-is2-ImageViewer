package es.ulpgc.imageviewer.architecture.commands;

import es.ulpgc.imageviewer.architecture.presenter.ImagePresenter;
import es.ulpgc.imageviewer.architecture.data.io.loaders.ImageFolderLoader;
import es.ulpgc.imageviewer.architecture.view.displays.ErrorDisplay;
import es.ulpgc.imageviewer.architecture.view.dialogs.FolderDialog;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static es.ulpgc.imageviewer.architecture.data.io.loaders.ImageFolderLoader.EmptyImageFolderException;

public record OpenImageFolderCommand(
        ErrorDisplay errorDisplay,
        FolderDialog folderDialog,
        ImagePresenter presenter
) implements Command {

    @Override
    public void execute() {
        Optional<File> folder = folderDialog.tryGet();
        if (folder.isEmpty()) return;

        Optional<ImageFolderLoader> loader = tryCreateLoader(folder.get());

        loader.ifPresent(presenter::showUsing);
    }

    private Optional<ImageFolderLoader> tryCreateLoader(File folder) {
        try {
            return Optional.of(new ImageFolderLoader(folder));
        } catch (EmptyImageFolderException | IOException e) {
            errorDisplay.show(e.getMessage());
            return Optional.empty();
        }
    }
}
