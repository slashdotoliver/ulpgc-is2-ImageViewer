package es.ulpgc.imageviewer.apps.swing.utils;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LookAndFeelHelper {
    public static void changeLookAndFeel(List<String> preferences) throws UnsupportedLookAndFeelException {
        Optional<String> look = findFirstOccurrence(
                preferences,
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
                IllegalAccessException e
        ) {
            throw new UnsupportedLookAndFeelException(e.getMessage());
        }
    }

    private static Optional<String> findFirstOccurrence(List<String> preferences, List<String> options) {
        return preferences.stream()
                .filter(preference -> options.stream()
                        .anyMatch(preference::equals))
                .findFirst();
    }
}
