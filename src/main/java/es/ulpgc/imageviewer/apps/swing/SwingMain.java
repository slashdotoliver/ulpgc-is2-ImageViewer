package es.ulpgc.imageviewer.apps.swing;

import es.ulpgc.imageviewer.apps.swing.model.SwingAppArguments;
import es.ulpgc.imageviewer.apps.swing.utils.LookAndFeelHelper;
import es.ulpgc.imageviewer.apps.swing.view.dialogs.SwingFolderDialog;
import es.ulpgc.imageviewer.apps.swing.view.displays.SwingSmoothImageDisplay;
import es.ulpgc.imageviewer.apps.swing.view.frames.SwingMainFrame;
import es.ulpgc.imageviewer.architecture.commands.CommandFactory;
import es.ulpgc.imageviewer.architecture.commands.OpenImageFolderCommand;
import es.ulpgc.imageviewer.architecture.presenter.ImagePresenter;
import es.ulpgc.imageviewer.architecture.presenter.SmoothImagePresenter;
import es.ulpgc.imageviewer.architecture.view.displays.ErrorDisplay;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static es.ulpgc.imageviewer.architecture.commands.CommandName.*;

public class SwingMain {

    private static final Logger LOGGER = Logger.getLogger(SwingMain.class.getSimpleName());
    private static final CommandRegistry COMMANDS = CommandRegistry.getInstance();
    private static ImagePresenter presenter;
    private static SwingMainFrame mainFrame;

    public static void main(String[] args) {
        changeLookAndFeel(List.of(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel",
                "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
        ));

        SwingSmoothImageDisplay imageDisplay = new SwingSmoothImageDisplay();
        mainFrame = new SwingMainFrame(imageDisplay, "Smooth Image Viewer");
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

    private static void registerCommands() {
        COMMANDS.register(OpenImageFolder, new OpenImageFolderCommand(
                        mainFrame.getErrorDisplay(),
                        new SwingFolderDialog(),
                        presenter
                ))
                .register(NextImage, new NextImageCommand(presenter))
                .register(PreviousImage, new PreviousImageCommand(presenter));
    }

    private static void tryShowing(File folder, ErrorDisplay errorDisplay) {
        new OpenImageFolderCommand(errorDisplay, () -> Optional.of(folder), presenter).execute();
    }

    private static void changeLookAndFeel(List<String> preferences) {
        try {
            LookAndFeelHelper.changeLookAndFeel(preferences);
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
