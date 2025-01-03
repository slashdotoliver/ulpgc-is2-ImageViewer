package es.ulpgc.imageviewer.apps.swing.model;

import java.io.File;

public record SwingAppArguments(String[] args) {

    public String validFormat() {
        return "Usage: <image-viewer> [initial directory]";
    }

    public boolean valid() {
        return args.length <= 1;
    }

    public boolean hasFolder() {
        return args.length == 1;
    }

    public File folder() {
        return new File(args[0]);
    }
}
