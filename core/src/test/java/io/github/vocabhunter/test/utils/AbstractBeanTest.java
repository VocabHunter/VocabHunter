/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.test.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractBeanTest<T> {
    protected abstract T buildPrimary();

    protected abstract T buildSecondary();

    @Test
    public void testIdentical() {
        T t = buildPrimary();
        assertEquals("Identical", t, t);
    }

    @Test
    public void testNotEqualToNull() {
        assertFalse("Not equal to null", buildPrimary().equals(null));
    }

    @Test
    public void testNotEqualToDifferentClass() {
        assertFalse("Not equal to different class", buildPrimary().equals(new Object()));
    }

    @Test
    public void testEqual() {
        assertEquals("Equal", buildPrimary(), buildPrimary());
    }

    @Test
    public void testNotEqual() {
        assertNotEquals("Not equal", buildPrimary(), buildSecondary());
    }

    @Test
    public void testEqualHashCode() {
        assertEquals("Equal hash code", buildPrimary().hashCode(), buildPrimary().hashCode());
    }

    @Test
    public void testToString() {
        assertTrue("To string", StringUtils.isNotBlank(buildPrimary().toString()));
    }
}
