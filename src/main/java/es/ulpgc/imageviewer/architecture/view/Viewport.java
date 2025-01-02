package es.ulpgc.imageviewer.architecture.view;

public record Viewport(
        int x,
        int y,
        int width,
        int height
) {
    public static Viewport from(int width, int height) {
        return new Viewport(0, 0, width, height);
    }

    private static double ratio(double width, double height) {
        return width / height;
    }

    public Viewport adjustedViewportFrom(int width, int height) {
        if (canFit(width, height)) return centeredViewportWith(width, height);
        return shouldScaleWidth(width, height)
                ? fitToWidthViewport(width, height)
                : fitToHeightViewport(width, height);
    }

    private Viewport centeredViewportWith(int width, int height) {
        return new Viewport(xCenterOf(width), yCenterOf(height), width, height);
    }

    private boolean shouldScaleWidth(int width, int height) {
        return ratio(width, height) > ratio(this.width, this.height);
    }

    private Viewport fitToWidthViewport(int width, int height) {
        return new Viewport(
                0,
                yCenterOf(calculateNewHeight(width, height)),
                this.width,
                calculateNewHeight(width, height)
        );
    }

    private Viewport fitToHeightViewport(int width, int height) {
        return new Viewport(
                xCenterOf(calculateNewWidth(width, height)),
                0,
                calculateNewWidth(width, height),
                this.height
        );
    }

    private int xCenterOf(int width) {
        return (this.width - width) / 2;
    }

    private int yCenterOf(int height) {
        return (this.height - height) / 2;
    }

    private int calculateNewWidth(int width, int height) {
        return this.height * width / height;
    }

    private int calculateNewHeight(int width, int height) {
        return this.width * height / width;
    }

    private boolean canFit(int width, int height) {
        return width <= this.width && height <= this.height;
    }
}
