/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

public final class FileListVersion {
    /**
     * First version of the format.
     */
    public static final int FORMAT_1 = 1;

    public static final int LATEST_VERSION = FORMAT_1;

    private FileListVersion() {
        // Prevent instantiation - only constants are defined.
    }
}
