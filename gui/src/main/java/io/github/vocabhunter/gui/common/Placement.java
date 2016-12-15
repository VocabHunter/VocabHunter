/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Placement placement = (Placement) o;

        return new EqualsBuilder()
            .append(isPositioned, placement.isPositioned)
            .append(width, placement.width)
            .append(height, placement.height)
            .append(x, placement.x)
            .append(y, placement.y)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(isPositioned)
            .append(width)
            .append(height)
            .append(x)
            .append(y)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("isPositioned", isPositioned)
            .append("width", width)
            .append("height", height)
            .append("x", x)
            .append("y", y)
            .toString();
    }
}
