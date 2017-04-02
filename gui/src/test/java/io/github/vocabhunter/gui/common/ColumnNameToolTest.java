/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnNameToolTest {
    @Test
    public void testA() {
        validate(0, "A");
    }

    @Test
    public void testB() {
        validate(1, "B");
    }

    @Test
    public void testZ() {
        validate(25, "Z");
    }

    @Test
    public void testAa() {
        validate(26, "AA");
    }

    @Test
    public void testAb() {
        validate(27, "AB");
    }

    @Test
    public void testZz() {
        validate(701, "ZZ");
    }

    @Test
    public void testAaa() {
        validate(702, "AAA");
    }

    @Test
    public void testAmj() {
        validate(1023, "AMJ");
    }

    private void validate(final int index, final String name) {
        String actual = ColumnNameTool.columnName(index);

        assertEquals("Column " + name, actual);
    }
}
