/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.test.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractBeanTest<T> {
    protected abstract T buildPrimary();

    protected abstract T buildSecondary();

    @Test
    public void testIdentical() {
        T t = buildPrimary();
        assertEquals(t, t, "Identical");
    }

    @Test
    @SuppressFBWarnings("EC_NULL_ARG")
    public void testNotEqualToNull() {
        assertFalse(buildPrimary().equals(null), "Not equal to null");
    }

    @Test
    public void testNotEqualToDifferentClass() {
        assertFalse(buildPrimary().equals(new Object()), "Not equal to different class");
    }

    @Test
    public void testEqual() {
        assertEquals(buildPrimary(), buildPrimary(), "Equal");
    }

    @Test
    public void testNotEqual() {
        assertNotEquals(buildPrimary(), buildSecondary(), "Not equal");
    }

    @Test
    public void testEqualHashCode() {
        assertEquals(buildPrimary().hashCode(), buildPrimary().hashCode(), "Equal hash code");
    }

    @Test
    public void testToString() {
        assertTrue(StringUtils.isNotBlank(buildPrimary().toString()), "To string");
    }
}
