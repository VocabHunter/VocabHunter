/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldValueToolTest {
    private final AtomicReference<String> field = new AtomicReference<>();

    @Test
    public void testGetAsIntegerUnchanged() {
        field.set("123");

        int result = FieldValueTool.getAsInteger(field::get, 0);

        assertEquals(123, result);
    }

    @Test
    public void testGetAsIntegerReplaceWithDefault() {
        field.set("BAD");

        int result = FieldValueTool.getAsInteger(field::get, 123);

        assertEquals(123, result);
    }

    @Test
    public void testApplyDefaultIfEmptyWithValue() {
        field.set("123");

        FieldValueTool.applyDefaultIfEmpty(field::set, field::get, () -> 0);

        assertEquals("123", field.get());
    }

    @Test
    public void testApplyDefaultIfEmptyWithoutValue() {
        field.set("");

        FieldValueTool.applyDefaultIfEmpty(field::set, field::get, () -> 123);

        assertEquals("123", field.get());
    }

    @Test
    public void testCleanNonNegativeIntegerUnchanged() {
        field.set("original");
        FieldValueTool.cleanNonNegativeInteger(field::set, "123", "0");

        assertEquals("original", field.get());
    }

    @Test
    public void testCleanNonNegativeIntegerEmpty() {
        field.set("original");
        FieldValueTool.cleanNonNegativeInteger(field::set, "", "0");

        assertEquals("original", field.get());
    }

    @Test
    public void testCleanNonNegativeIntegerBadValue() {
        field.set("original");
        FieldValueTool.cleanNonNegativeInteger(field::set, "bad", "123");

        assertEquals("123", field.get());
    }

    @Test
    public void testCleanNonNegativeIntegerNegativeValue() {
        field.set("original");
        FieldValueTool.cleanNonNegativeInteger(field::set, "-1", "123");

        assertEquals("123", field.get());
    }
}
