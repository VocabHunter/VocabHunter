/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.test.utils.AbstractBeanTest;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileChoiceTest extends AbstractBeanTest<FileChoice> {
    private static final Path FILE = Paths.get("test");

    private static final FileFormatType PRIMARY_TYPE = FileFormatType.DOCUMENT;

    private static final FileFormatType SECONDARY_TYPE = FileFormatType.SPREADSHEET;

    private static final FileChoice PRIMARY = new FileChoice(FILE, PRIMARY_TYPE);

    private static final FileChoice SECONDARY = new FileChoice(FILE, SECONDARY_TYPE);

    @Override
    protected FileChoice buildPrimary() {
        return PRIMARY;
    }

    @Override
    protected FileChoice buildSecondary() {
        return SECONDARY;
    }

    @Test
    public void testGetFile() {
        assertEquals(FILE, PRIMARY.getFile());
    }

    @Test
    public void testGetType() {
        assertEquals(PRIMARY_TYPE, PRIMARY.getType());
    }
}
