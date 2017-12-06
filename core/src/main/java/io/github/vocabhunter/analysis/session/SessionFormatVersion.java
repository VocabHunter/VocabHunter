/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

public final class SessionFormatVersion {
    /**
     * First version of the format.
     */
    public static final int FORMAT_1 = 1;

    /**
     * Added the use count for each word.
     */
    public static final int FORMAT_2 = 2;

    /**
     * Always capitalised words are preserved.
     */
    public static final int FORMAT_3 = 3;

    /**
     * Switched from storing lines within words to just storing line numbers.
     */
    public static final int FORMAT_4 = 4;

    /**
     * Words can now have notes added to them.
     */
    public static final int FORMAT_5 = 5;

    public static final int LATEST_VERSION = FORMAT_5;

    private SessionFormatVersion() {
        // Prevent instantiation - only constants are defined.
    }
}
