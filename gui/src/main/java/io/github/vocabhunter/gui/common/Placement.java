/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

public record Placement(boolean positioned, double width, double height, double x, double y) {
    public Placement(final double width, final double height, final double x, final double y) {
        this(true, width, height, x, y);
    }

    public Placement(final double width, final double height) {
        this(false, width, height, -1, -1);
    }
}
