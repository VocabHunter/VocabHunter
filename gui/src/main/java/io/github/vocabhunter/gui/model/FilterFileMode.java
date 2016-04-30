/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

public enum FilterFileMode {
    KNOWN("Known Words", false), SEEN("Known & Unknown Words", true);

    private final String name;

    private final boolean isIncludeUnknown;

    FilterFileMode(final String name, final boolean isIncludeUnknown) {
        this.name = name;
        this.isIncludeUnknown = isIncludeUnknown;
    }

    public boolean isIncludeUnknown() {
        return isIncludeUnknown;
    }

    @Override
    public String toString() {
        return name;
    }

    public static FilterFileMode getMode(final boolean includeUnknown) {
        if (includeUnknown) {
            return SEEN;
        } else {
            return KNOWN;
        }
    }
}
