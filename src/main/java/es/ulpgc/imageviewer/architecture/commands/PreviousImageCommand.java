package es.ulpgc.imageviewer.architecture.commands;

import es.ulpgc.imageviewer.architecture.presenter.ImagePresenter;

public record PreviousImageCommand(ImagePresenter presenter) implements Command {
    @Override
    public void execute() {
        presenter.showPrevious();
    }
}
