/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public final class SessionWordsTool {
    private SessionWordsTool() {
        // Prevent instantiation - all methods are static
    }

    public static List<String> knownWords(final Path file) {
        return knownWords(readMarkedWords(file));
    }

    public static List<String> knownWords(final List<? extends MarkedWord> words) {
        return words(words, SessionWordsTool::isKnown);
    }

    public static List<String> seenWords(final Path file) {
        return seenWords(readMarkedWords(file));
    }

    public static List<String> seenWords(final List<? extends MarkedWord> words) {
        return words(words, SessionWordsTool::isSeen);
    }

    private static List<String> words(final List<? extends MarkedWord> words, final Predicate<MarkedWord> wordFilter) {
        return words.stream()
            .filter(wordFilter)
            .map(MarkedWord::getWordIdentifier)
            .collect(toList());
    }

    private static boolean isKnown(final MarkedWord w) {
        return w.getState().equals(WordState.KNOWN);
    }

    private static boolean isSeen(final MarkedWord w) {
        WordState state = w.getState();

        return state.equals(WordState.KNOWN) || state.equals(WordState.UNKNOWN);
    }

    private static List<? extends MarkedWord> readMarkedWords(final Path file) {
        try {
            return SessionSerialiser.readMarkedWords(file);
        } catch (Exception e) {
            throw new VocabHunterException(String.format("Unable to read filter file '%s'", file), e);
        }
    }
}

