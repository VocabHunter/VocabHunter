/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class SettingsPathTool {
    private static final String VOCAB_HUNTER = "VocabHunter";

    private SettingsPathTool() {
        // Prevent instantiation - all methods are static
    }

    public static Path obtainSettingsFilePath(final String filename) {
        if (SystemUtils.IS_OS_WINDOWS && StringUtils.isNotBlank(getWindowsAppHome())) {
            return Paths.get(getWindowsAppHome(), VOCAB_HUNTER, filename);
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            return Paths.get(getUserHome(), "Library", VOCAB_HUNTER, filename);
        } else {
            return Paths.get(getUserHome(), ".VocabHunter", filename);
        }
    }

    private static String getUserHome() {
        String home = System.getProperty("user.home");

        if (StringUtils.isBlank(home)) {
            throw new VocabHunterException("Unable to find user home directory");
        } else {
            return home;
        }
    }

    private static String getWindowsAppHome() {
        return System.getenv("APPDATA");
    }
}
