/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.gui.common.SequencedWord;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class SearchTool {
    private SearchTool() {
        // Prevent instantiation - all methods are static
    }

    public static Predicate<SequencedWord> matchMaker(final String text) {
        String search = normalise(text.trim());

        return w -> isMatch(w, search);
    }

    private static boolean isMatch(final SequencedWord w, final String searchText) {
        return normalise(w.getWordIdentifier()).contains(searchText);
    }

    private static String normalise(final String s) {
        return CoreTool.toLowerCase(StringUtils.stripAccents(s));
    }

    public static int getMatchIndex(final List<? extends SequencedWord> matches, final SequencedWord currentWord) {
        return IntStream.range(0, matches.size())
                .filter(i -> matches.get(i).equals(currentWord))
                .findFirst()
                .orElse(-1);
    }

    public static int getPreviousMatchIndex(final List<? extends SequencedWord> matches, final SequencedWord currentWord) {
        int currentSequenceNo = currentWord.getSequenceNo();
        int matchCount = matches.size();

        return IntStream.range(0, matchCount)
            .map(i -> matchCount - i - 1)
            .filter(i -> matches.get(i).getSequenceNo() < currentSequenceNo)
            .findFirst()
            .orElse(-1);
    }
}
