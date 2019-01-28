/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import java.util.Objects;

public final class GuiNoteTool {
    private GuiNoteTool() {
        // Prevent instantiation - all methods are static
    }

    public static String toGuiNote(final String note) {
        return Objects.requireNonNullElse(note, "");
    }

    public static String fromGuiNote(final String note) {
        if ("".equals(note)) {
            return null;
        } else {
            return note;
        }
    }
}
