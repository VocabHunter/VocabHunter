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

    private static String classifier(final AnalysisWord use) {
        return CoreTool.toLowerCase(use.getWordIdentifier());
    }
}
