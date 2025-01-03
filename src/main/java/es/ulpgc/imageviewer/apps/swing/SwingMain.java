package es.ulpgc.imageviewer.apps.swing;

import es.ulpgc.imageviewer.apps.swing.model.SwingAppArguments;
import es.ulpgc.imageviewer.apps.swing.view.SwingFolderDialog;
import es.ulpgc.imageviewer.apps.swing.view.SwingMainFrame;
import es.ulpgc.imageviewer.apps.swing.view.SwingSmoothImageDisplay;
import es.ulpgc.imageviewer.architecture.control.commands.Command;
import es.ulpgc.imageviewer.architecture.control.commands.CommandName;
import es.ulpgc.imageviewer.architecture.control.commands.OpenImageFolderCommand;
import es.ulpgc.imageviewer.architecture.control.io.FolderImageLoader;
import es.ulpgc.imageviewer.architecture.control.presenters.ImagePresenter;
import es.ulpgc.imageviewer.architecture.control.presenters.SmoothImagePresenter;
import es.ulpgc.imageviewer.architecture.view.ErrorDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static es.ulpgc.imageviewer.architecture.control.io.FolderImageLoader.EmptyImageFolderException;

public class SwingMain {

    private static final Map<CommandName, Command> COMMANDS = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(SwingMain.class.getSimpleName());
    private static ImagePresenter presenter;
    private static SwingMainFrame mainFrame;

    public static void main(String[] args) {
        changeLookAndFeel();

        SwingSmoothImageDisplay imageDisplay = new SwingSmoothImageDisplay();
        mainFrame = new SwingMainFrame(imageDisplay);
        presenter = new SmoothImagePresenter(imageDisplay);

        handleArguments(args);
        registerOpenFolderCommand();
        mainFrame.setVisible(true);
    }

    private static void handleArguments(String[] args) {
        SwingAppArguments arguments = new SwingAppArguments(args);
        if (!arguments.valid())
            System.err.println(arguments.validFormat());
        else if (arguments.hasFolder())
            tryShowing(arguments.folder(), mainFrame.getErrorDisplay());
    }

    private static void registerOpenFolderCommand() {
        COMMANDS.put(CommandName.OpenFolderCommand, new OpenImageFolderCommand(
                mainFrame.getErrorDisplay(),
                new SwingFolderDialog(),
                presenter
        ));
        mainFrame.getOpenFolderMenuItem().addActionListener(
                _ -> COMMANDS.get(CommandName.OpenFolderCommand).execute()
        );
    }

    private static void tryShowing(File folder, ErrorDisplay errorDisplay) {
        try {
            presenter.showUsing(new FolderImageLoader(folder));
        } catch (IOException | EmptyImageFolderException e) {
            errorDisplay.show(e.getMessage());
        }
    }

    private static void changeLookAndFeel() {
        Optional<String> look = findFirstOccurrence(
                List.of(
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel",
                        "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
                ),
                Arrays.stream(UIManager.getInstalledLookAndFeels())
                        .map(UIManager.LookAndFeelInfo::getClassName)
                        .toList()
        );
        if (look.isEmpty()) return;
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

    private static Optional<String> findFirstOccurrence(List<String> preferences, List<String> options) {
        return preferences.stream()
                .filter(preference -> options.stream()
                        .anyMatch(preference::equals))
                .findFirst();
    }
}
