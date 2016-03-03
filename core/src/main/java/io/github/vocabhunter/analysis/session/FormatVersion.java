/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

public final class FormatVersion {
    /**
     * First version of the format.
     */
    public static final int FORMAT_1 = 1;

    /**
     * Added the use count for each word.
     */
    public static final int FORMAT_2 = 2;

    public static final int MAX_SUPPORTED_VERSION = FORMAT_2;

    private FormatVersion() {
        // Prevent instantiation - only constants are defined.
    }
}
