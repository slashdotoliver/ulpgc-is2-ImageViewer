package es.ulpgc.imageviewer.apps.swing.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JPanelBuilder {

    public interface LayoutConfigurator {
        void configure(JPanel panel);
    }

    private final LayoutConfigurator layoutConfigurator;
    private final List<Component> components = new ArrayList<>();
    private Optional<Boolean> opaque = Optional.empty();
    private Optional<Color> background = Optional.empty();

    public JPanelBuilder(LayoutConfigurator layoutConfigurator) {
        this.layoutConfigurator = layoutConfigurator;
    }

    public static JPanelBuilder withBoxLayout(int axis) {
        return new JPanelBuilder(panel -> panel.setLayout(new BoxLayout(panel, axis)));
    }

    public static JPanelBuilder withFlowLayout() {
        return new JPanelBuilder(panel -> panel.setLayout(new FlowLayout()));
    }

    public JPanelBuilder setOpaque(boolean value) {
        this.opaque = Optional.of(value);
        return this;
    }

    public JPanelBuilder add(Component... components) {
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    public JPanelBuilder setBackground(Color backgroundColor) {
        this.background = Optional.ofNullable(backgroundColor);
        return this;
    }

    public JPanel build() {
        JPanel panel = new JPanel();
        layoutConfigurator.configure(panel);
        components.forEach(panel::add);
        opaque.ifPresent(panel::setOpaque);
        background.ifPresent(panel::setBackground);
        return panel;
    }
}
