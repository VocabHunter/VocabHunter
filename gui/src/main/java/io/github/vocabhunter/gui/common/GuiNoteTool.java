/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

public final class GuiNoteTool {
    private GuiNoteTool() {
        // Prevent instantiation - all methods are static
    }

    public static String toGuiNote(final String note) {
        if (note == null) {
            return "";
        } else {
            return note;
        }
    }

    public static String fromGuiNote(final String note) {
        if ("".equals(note)) {
            return null;
        } else {
            return note;
        }
    }
}
