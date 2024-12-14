package imageviewer.apps.swingsimple;

import imageviewer.apps.swingsimple.control.ProgramArguments;
import imageviewer.apps.swingsimple.view.SimpleFolderDialog;
import imageviewer.apps.swingsimple.view.SimpleMainFrame;
import imageviewer.architecture.control.ImagePresenter;
import imageviewer.architecture.control.commands.Command;
import imageviewer.architecture.control.commands.CommandName;
import imageviewer.architecture.control.commands.OpenImageFolderCommand;
import imageviewer.architecture.control.io.FolderImageLoader;
import imageviewer.architecture.view.ErrorDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class SimpleMain {

    private static final Map<CommandName, Command> COMMANDS = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(SimpleMain.class.getSimpleName());
    private static ImagePresenter presenter;
    private static SimpleMainFrame mainFrame;

    public static void main(String[] args) {
        changeLookAndFeel();

        mainFrame = new SimpleMainFrame();
        presenter = new ImagePresenter(mainFrame.getImageDisplay());

        handleArguments(args);
        registerOpenFolderCommand();
        mainFrame.setVisible(true);
    }

    private static void handleArguments(String[] args) {
        ProgramArguments arguments = new ProgramArguments(args);
        if (!arguments.valid())
            System.err.println(arguments.validFormat());
        else if (arguments.hasFolder())
            tryShowing(arguments.folder(), mainFrame.getErrorDisplay());
    }

    private static void registerOpenFolderCommand() {
        COMMANDS.put(CommandName.OpenFolderCommand, new OpenImageFolderCommand(
                mainFrame.getErrorDisplay(),
                new SimpleFolderDialog(),
                presenter
        ));
        mainFrame.getOpenFolderMenuItem().addActionListener(
                _ -> COMMANDS.get(CommandName.OpenFolderCommand).execute()
        );
    }

    private static void tryShowing(File folder, ErrorDisplay errorDisplay) {
        try {
            presenter.loadUsing(new FolderImageLoader(folder));
        } catch (IOException | FolderImageLoader.EmptyImageFolderException e) {
            errorDisplay.show(e.getMessage());
        }
    }

    private static void changeLookAndFeel() {
        Optional<String> look = findFirstOccurrence(
                List.of(
                        "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                ),
                Arrays.stream(UIManager.getInstalledLookAndFeels())
                        .map(UIManager.LookAndFeelInfo::getClassName)
                        .toList()
        );
        if (look.isPresent()) {
            try {
                UIManager.setLookAndFeel(look.get());
            } catch (
                    ClassNotFoundException |
                    InstantiationException |
                    IllegalAccessException |
                    UnsupportedLookAndFeelException e
            ) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    private static Optional<String> findFirstOccurrence(List<String> preferences, List<String> options) {
        return preferences.stream()
                .filter(preference -> options.stream()
                        .anyMatch(preference::equals))
                .findFirst();
    }
}
