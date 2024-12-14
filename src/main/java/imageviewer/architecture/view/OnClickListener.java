package imageviewer.architecture.view;

public interface OnClickListener {
    OnClickListener None = () -> {};

    void clickPerformed();
}
