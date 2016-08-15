/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

public final class Placement {
    private final boolean isPositioned;

    private final double width;

    private final double height;

    private final double x;

    private final double y;

    public Placement(final double width, final double height, final double x, final double y) {
        this(true, width, height, x, y);
    }

    public Placement(final double width, final double height) {
        this(false, width, height, -1, -1);
    }

    private Placement(final boolean isPositioned, final double width, final double height, final double x, final double y) {
        this.isPositioned = isPositioned;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public boolean isPositioned() {
        return isPositioned;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
