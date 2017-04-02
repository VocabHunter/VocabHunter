/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ExcelListedFileTest extends AbstractBeanTest<ExcelListedFile> {
    private static final Path FILE = Paths.get("file1");

    @Override
    protected ExcelListedFile buildPrimary() {
        return new ExcelListedFile(FILE, Arrays.asList(1, 2));
    }

    @Override
    protected ExcelListedFile buildSecondary() {
        return new ExcelListedFile(FILE, Arrays.asList(1, 2, 3));
    }
}
