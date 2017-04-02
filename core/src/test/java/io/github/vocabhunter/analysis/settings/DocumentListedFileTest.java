/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentListedFileTest extends AbstractBeanTest<DocumentListedFile> {
    private static final Path FILE_1 = Paths.get("file1");

    private static final Path FILE_2 = Paths.get("file2");

    @Override
    protected DocumentListedFile buildPrimary() {
        return new DocumentListedFile(FILE_1);
    }

    @Override
    protected DocumentListedFile buildSecondary() {
        return new DocumentListedFile(FILE_2);
    }
}
