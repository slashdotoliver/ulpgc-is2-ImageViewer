package imageviewer.architecture.control.commands;

import imageviewer.architecture.control.ImagePresenter;
import imageviewer.architecture.control.io.FolderImageLoader;
import imageviewer.architecture.view.ErrorDisplay;
import imageviewer.architecture.view.FolderDialog;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static imageviewer.architecture.control.io.FolderImageLoader.EmptyImageFolderException;

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

        loader.ifPresent(presenter::loadUsing);
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