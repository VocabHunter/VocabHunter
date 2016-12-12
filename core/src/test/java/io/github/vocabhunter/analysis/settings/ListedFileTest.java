/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.vocabhunter.analysis.settings.ListedFileType.SESSION;

public class ListedFileTest extends AbstractBeanTest<ListedFile> {
    private static final Path FILE_1 = Paths.get("file1");

    private static final Path FILE_2 = Paths.get("file2");

    @Override
    protected ListedFile buildPrimary() {
        return new ListedFile(FILE_1, SESSION, true);
    }

    @Override
    protected ListedFile buildSecondary() {
        return new ListedFile(FILE_2, SESSION, true);
    }
}
