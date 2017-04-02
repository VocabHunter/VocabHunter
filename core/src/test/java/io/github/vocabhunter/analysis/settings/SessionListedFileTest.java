/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionListedFileTest extends AbstractBeanTest<SessionListedFile> {
    private static final Path FILE_1 = Paths.get("file1");

    private static final Path FILE_2 = Paths.get("file2");

    @Override
    protected SessionListedFile buildPrimary() {
        return new SessionListedFile(FILE_1, true);
    }

    @Override
    protected SessionListedFile buildSecondary() {
        return new SessionListedFile(FILE_2, true);
    }
}
