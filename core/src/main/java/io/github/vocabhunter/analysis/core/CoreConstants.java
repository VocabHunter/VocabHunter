/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class CoreConstants {
    public static final Locale LOCALE = Locale.UK;

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private CoreConstants() {
        // Prevent instantiation - only constants are defined.
    }
}
