/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

public enum FilterFileMode {
    KNOWN("Known Words"), SEEN("Known & Unknown Words");

    private final String name;

    FilterFileMode(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
