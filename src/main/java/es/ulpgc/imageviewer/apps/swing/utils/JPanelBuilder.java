package es.ulpgc.imageviewer.apps.swing.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JPanelBuilder {

    private interface PanelConfigurator {
        void configure(JPanel panel);
    }

    private final PanelConfigurator panelConfigurator;
    private final List<Component> components = new ArrayList<>();
    private Optional<Boolean> opaque = Optional.empty();
    private Optional<Color> background = Optional.empty();

    private JPanelBuilder(PanelConfigurator panelConfigurator) {
        this.panelConfigurator = panelConfigurator;
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
        panelConfigurator.configure(panel);
        components.forEach(panel::add);
        opaque.ifPresent(panel::setOpaque);
        background.ifPresent(panel::setBackground);
        return panel;
    }
}
