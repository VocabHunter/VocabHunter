/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class WordUseTool {
    private static final Pattern PATTERN = Pattern.compile("\\p{javaLetter}");

    private final String word;

    private final int wordLength;

    private final String use;

    public WordUseTool(final String word, final String use) {
        this.word = word;
        this.use = use;
        wordLength = word.length();
    }

    public Stream<String> stream() {
        List<String> result = new ArrayList<>();
        String remaining = use;

        do {
            int next = nextMatch(remaining);

            if (next == -1) {
                result.add(remaining);
                remaining = "";
            } else {
                if (next != 0) {
                    result.add(remaining.substring(0, next));
                    remaining = remaining.substring(next);
                }
                result.add(remaining.substring(0, wordLength));
                remaining = remaining.substring(wordLength);
            }
        }
        while (!"".equals(remaining));

        return result.stream();
    }

    private int nextMatch(final String remaining) {
        int from = 0;
        int next = candidate(remaining, from);

        while (next != -1 && isFalsePositive(remaining, next)) {
            from = next + 1;
            next = candidate(remaining, from);
        }

        return next;
    }

    private int candidate(final String remaining, final int from) {
        return remaining.toUpperCase(Locale.ENGLISH).indexOf(word.toUpperCase(Locale.ENGLISH), from);
    }

    private boolean isFalsePositive(final String match, final int start) {
        return isCharacterMatch(match, start - 1) || isCharacterMatch(match, start + wordLength);
    }

    private boolean isCharacterMatch(final String match, final int index) {
        return index >= 0 && index < match.length() && PATTERN.matcher(match.substring(index, index + 1)).matches();
    }
}
