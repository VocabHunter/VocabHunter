/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilterFileTest extends AbstractBeanTest<FilterFile> {
    private static final Path FILE = Paths.get("emtpy");

    @Override
    protected FilterFile buildPrimary() {
        return new FilterFile(FILE, FilterFileMode.KNOWN);
    }

    @Override
    protected FilterFile buildSecondary() {
        return new FilterFile(FILE, FilterFileMode.SEEN);
    }
}
