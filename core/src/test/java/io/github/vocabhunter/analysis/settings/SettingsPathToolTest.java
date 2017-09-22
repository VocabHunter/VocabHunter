/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SettingsPathToolTest {
    private static final Logger LOG = LoggerFactory.getLogger(SettingsPathToolTest.class);

    @Test
    public void testObtainSettingsFilePath() {
        Path path = SettingsPathTool.obtainSettingsFilePath("test.txt");

        LOG.info("Settings file path {}", path);

        Assertions.assertNotNull(path, "Settings file path");
    }
}
