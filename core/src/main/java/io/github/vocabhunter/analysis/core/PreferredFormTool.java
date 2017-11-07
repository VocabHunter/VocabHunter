/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

public final class PreferredFormTool {
    private PreferredFormTool() {
        // Prevent instantiation - all methods are static
    }

    public static String preferredForm(final String s1, final String s2) {
        if (s1.equals(s2)) {
            return s1;
        } else {
            int length = s1.length();
            boolean isFirst = true;
            boolean isSecond = true;

            for (int i = 0; i < length; ++i) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);

                if (c1 != c2) {
                    boolean isFirstLower = Character.isLowerCase(c1);

                    isFirst &= isFirstLower;
                    isSecond &= !isFirstLower;
                }
            }

            return preferredForm(s1, s2, isFirst, isSecond);
        }
    }

    private static String preferredForm(final String s1, final String s2, final boolean isFirst, final boolean isSecond) {
        if (isFirst) {
            return s1;
        } else if (isSecond) {
            return s2;
        } else {
            return CoreTool.toLowerCase(s1);
        }
    }
}
