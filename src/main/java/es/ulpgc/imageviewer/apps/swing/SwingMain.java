package es.ulpgc.imageviewer.apps.swing;

import es.ulpgc.imageviewer.apps.swing.data.SwingImageCache;
import es.ulpgc.imageviewer.apps.swing.data.SwingImageCachedConverter;
import es.ulpgc.imageviewer.apps.swing.data.SwingImageConverter;
import es.ulpgc.imageviewer.apps.swing.data.io.SwingImageDeserializer;
import es.ulpgc.imageviewer.apps.swing.model.SwingAppArguments;
import es.ulpgc.imageviewer.apps.swing.utils.LookAndFeelHelper;
import es.ulpgc.imageviewer.apps.swing.view.dialogs.SwingFolderDialog;
import es.ulpgc.imageviewer.apps.swing.view.displays.SwingSimpleImageDisplay;
import es.ulpgc.imageviewer.apps.swing.view.displays.SwingSmoothImageDisplay;
import es.ulpgc.imageviewer.apps.swing.view.frames.SwingMainFrame;
import es.ulpgc.imageviewer.architecture.commands.CommandRegistry;
import es.ulpgc.imageviewer.architecture.commands.NextImageCommand;
import es.ulpgc.imageviewer.architecture.commands.OpenImageFolderCommand;
import es.ulpgc.imageviewer.architecture.commands.PreviousImageCommand;
import es.ulpgc.imageviewer.architecture.model.entities.Image;
import es.ulpgc.imageviewer.architecture.presenter.ImagePresenter;
import es.ulpgc.imageviewer.architecture.presenter.SimpleImagePresenter;
import es.ulpgc.imageviewer.architecture.presenter.SmoothImagePresenter;
import es.ulpgc.imageviewer.architecture.view.displays.ErrorDisplay;
import es.ulpgc.imageviewer.architecture.view.displays.ImageDisplayStyle;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static es.ulpgc.imageviewer.architecture.commands.CommandName.*;

public class SwingMain {

    private static final List<String> LOOK_PREFERENCES = List.of(
            "javax.swing.plaf.nimbus.NimbusLookAndFeel",
            "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
    );
    private static final Logger LOGGER = Logger.getLogger(SwingMain.class.getSimpleName());
    private static final CommandRegistry COMMANDS = CommandRegistry.getInstance();
    private static ImagePresenter presenter;
    private static SwingMainFrame mainFrame;

    public static void main(String[] args) {
        changeLookAndFeel();
        configureWith(ImageDisplayStyle.Smooth);
        handleArguments(args);
        registerCommands();
        mainFrame.setVisible(true);
    }

    private static void configureWith(ImageDisplayStyle style) {
        final SwingImageCachedConverter converter = new SwingImageCachedConverter(
                new SwingImageCache<String>().withKeyMapping(Image::name),
                new SwingImageConverter(new SwingImageDeserializer())
        );
        switch (style) {
            case Simple -> {
                SwingSimpleImageDisplay imageDisplay = new SwingSimpleImageDisplay(converter);
                mainFrame = new SwingMainFrame(imageDisplay, "Simple Image Viewer");
                presenter = new SimpleImagePresenter(imageDisplay);
            }
            case Smooth -> {
                SwingSmoothImageDisplay imageDisplay = new SwingSmoothImageDisplay(converter);
                mainFrame = new SwingMainFrame(imageDisplay, "Smooth Image Viewer");
                presenter = new SmoothImagePresenter(imageDisplay);
            }
        }
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
                .register(ShowNextImage, new NextImageCommand(presenter))
                .register(ShowPreviousImage, new PreviousImageCommand(presenter));
    }

    private static void tryShowing(File folder, ErrorDisplay errorDisplay) {
        new OpenImageFolderCommand(errorDisplay, () -> Optional.of(folder), presenter).execute();
    }

    private static void changeLookAndFeel() {
        try {
            LookAndFeelHelper.changeLookAndFeel(LOOK_PREFERENCES);
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
