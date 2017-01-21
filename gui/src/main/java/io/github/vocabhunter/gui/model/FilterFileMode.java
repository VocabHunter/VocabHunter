/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

public enum FilterFileMode {
    KNOWN(false), SEEN(true);

    private final boolean isIncludeUnknown;

    FilterFileMode(final boolean isIncludeUnknown) {
        this.isIncludeUnknown = isIncludeUnknown;
    }

    public boolean isIncludeUnknown() {
        return isIncludeUnknown;
    }

    public static FilterFileMode getMode(final boolean includeUnknown) {
        if (includeUnknown) {
            return SEEN;
        } else {
            return KNOWN;
        }
    }
}
