/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WindowSettings {
    private double width;

    private double height;

    private double x;

    private double y;

    private double splitUsePosition = 0.55;

    private double splitWordPosition = 0.65;

    public double getWidth() {
        return width;
    }

    public void setWidth(final double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(final double height) {
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getSplitUsePosition() {
        return splitUsePosition;
    }

    public void setSplitUsePosition(final double splitUsePosition) {
        this.splitUsePosition = splitUsePosition;
    }

    public double getSplitWordPosition() {
        return splitWordPosition;
    }

    public void setSplitWordPosition(final double splitWordPosition) {
        this.splitWordPosition = splitWordPosition;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WindowSettings that = (WindowSettings) o;

        return new EqualsBuilder()
            .append(width, that.width)
            .append(height, that.height)
            .append(x, that.x)
            .append(y, that.y)
            .append(splitUsePosition, that.splitUsePosition)
            .append(splitWordPosition, that.splitWordPosition)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(width)
            .append(height)
            .append(x)
            .append(y)
            .append(splitUsePosition)
            .append(splitWordPosition)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("width", width)
            .append("height", height)
            .append("x", x)
            .append("y", y)
            .append("splitUsePosition", splitUsePosition)
            .append("splitWordPosition", splitWordPosition)
            .toString();
    }
}
