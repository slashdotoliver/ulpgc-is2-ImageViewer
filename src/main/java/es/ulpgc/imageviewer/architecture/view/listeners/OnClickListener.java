package es.ulpgc.imageviewer.architecture.view.listeners;

public interface OnClickListener {
    OnClickListener None = () -> { };

    void clickPerformed();
}
