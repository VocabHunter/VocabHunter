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

    public static final int LATEST_VERSION = FORMAT_3;

    private SessionFormatVersion() {
        // Prevent instantiation - only constants are defined.
    }
}
