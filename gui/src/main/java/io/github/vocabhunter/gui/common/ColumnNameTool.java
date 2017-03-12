/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

public final class ColumnNameTool {
    private static final int ALPHABET_LENGTH = 26;

    private ColumnNameTool() {
        // Prevent instantiation - all methods are static
    }

    public static String columnName(final int index) {
        return "Column " + letters(index);
    }

    private static String letters(final int index) {
        char c = (char) ('A' + (index % ALPHABET_LENGTH));

        if (index / 26 == 0) {
            return Character.toString(c);
        } else {
            return letters(index / 26 - 1) + c;
        }
    }
}
