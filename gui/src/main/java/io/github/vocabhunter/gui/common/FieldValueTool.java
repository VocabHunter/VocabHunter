/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FieldValueTool {
    private static final Logger LOG = LoggerFactory.getLogger(FieldValueTool.class);

    private FieldValueTool() {
        // Prevent instantiation - all methods are static
    }

    public static int getAsInteger(final Supplier<String> fieldGetter, final int defaultValue) {
        String text = fieldGetter.get();

        try {
            return Integer.parseInt(text);
        } catch (final NumberFormatException e) {
            LOG.debug("Illegal field value", e);

            return defaultValue;
        }
    }

    public static void applyDefaultIfEmpty(final Consumer<String> fieldSetter, final Supplier<String> fieldGetter, final Supplier<Object> defaultGetter) {
        String text = fieldGetter.get();

        if (text.isEmpty()) {
            fieldSetter.accept(defaultGetter.get().toString());
        }
    }

    public static void cleanNonNegativeInteger(final Consumer<String> fieldSetter, final String newValue, final String oldValue) {
        String clean = getAsCleanNonNegativeInteger(oldValue, newValue);

        if (!clean.equals(newValue)) {
            fieldSetter.accept(clean);
        }
    }

    private static String getAsCleanNonNegativeInteger(final String oldValue, final String newValue) {
        if (newValue.isEmpty()) {
            return newValue;
        } else {
            try {
                int n = Integer.parseInt(newValue);

                if (n >= 0) {
                    return Integer.toString(n);
                }
            } catch (final NumberFormatException e) {
                LOG.debug("Illegal field value", e);
            }

            return oldValue;
        }
    }
}
