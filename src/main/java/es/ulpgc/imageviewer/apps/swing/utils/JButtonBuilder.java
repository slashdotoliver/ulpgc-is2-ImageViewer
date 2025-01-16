package es.ulpgc.imageviewer.apps.swing.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Optional;

import static java.util.Optional.*;

public class JButtonBuilder {

    private Optional<String> text = empty();
    private Optional<Dimension> maximumSize = empty();
    private Optional<Dimension> preferredSize = empty();
    private Optional<KeyListener> keyListener = empty();
    private Optional<ActionListener> actionListener = empty();

    public JButtonBuilder setText(String text) {
        this.text = ofNullable(text);
        return this;
    }

    public JButtonBuilder setKeyListener(KeyListener listener) {
        this.keyListener = ofNullable(listener);
        return this;
    }

    public JButtonBuilder setMaximumSize(Dimension maximumSize) {
        this.maximumSize = Optional.ofNullable(maximumSize);
        return this;
    }

    public JButtonBuilder setPreferredSize(Dimension preferredSize) {
        this.preferredSize = Optional.ofNullable(preferredSize);
        return this;
    }

    public JButtonBuilder setActionListener(ActionListener listener) {
        this.actionListener = ofNullable(listener);
        return this;
    }

    public JButton build() {
        JButton button = new JButton();
        text.ifPresent(button::setText);
        keyListener.ifPresent(button::addKeyListener);
        maximumSize.ifPresent(button::setMaximumSize);
        preferredSize.ifPresent(button::setPreferredSize);
        actionListener.ifPresent(button::addActionListener);
        return button;
    }
}
