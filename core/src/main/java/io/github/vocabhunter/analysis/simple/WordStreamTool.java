/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public final class WordStreamTool {
    private static final Pattern PATTERN = Pattern.compile(
        "^\\P{javaLetter}+|\\P{javaLetter}+$|\\P{javaLetter}*\\p{javaWhitespace}\\P{javaLetter}*|\\P{javaLetter}\\P{javaLetter}+");

    public static final Comparator<AnalysisWord> WORD_COMPARATOR
        = comparing(AnalysisWord::getUseCount).reversed().thenComparing(WordStreamTool::classifier);

    private WordStreamTool() {
        // Prevent instantiation - all methods are private
    }

    public static Stream<String> words(final String line) {
        return PATTERN.splitAsStream(line)
            .filter(w -> !w.isEmpty());
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

    public static String classifier(final AnalysisWord use) {
        return CoreTool.toLowerCase(use.getWordIdentifier());
    }
}
