/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public final class SessionWordsTool {
    private SessionWordsTool() {
        // Prevent instantiation - all methods are static
    }

    public static List<String> knownWords(final Path file) {
        return knownWords(readSessionFile(file));
    }

    public static List<String> knownWords(final EnrichedSessionState state) {
        return knownWords(state.getState());
    }

    private static List<String> knownWords(final SessionState state) {
        return words(state, SessionWordsTool::isKnown);
    }

    public static List<String> seenWords(final Path file) {
        return seenWords(readSessionFile(file));
    }

    public static List<String> seenWords(final EnrichedSessionState state) {
        return seenWords(state.getState());
    }

    private static List<String> seenWords(final SessionState state) {
        return words(state, SessionWordsTool::isSeen);
    }

    private static List<String> words(final SessionState state, final Predicate<SessionWord> wordFilter) {
        return state.getOrderedUses().stream()
            .filter(wordFilter)
            .map(AnalysisWord::getWordIdentifier)
            .collect(toList());
    }

    private static boolean isKnown(final SessionWord w) {
        return w.getState().equals(WordState.KNOWN);
    }

    private static boolean isSeen(final SessionWord w) {
        WordState state = w.getState();

        return state.equals(WordState.KNOWN) || state.equals(WordState.UNKNOWN);
    }

    private static EnrichedSessionState readSessionFile(final Path file) {
        try {
            return SessionSerialiser.read(file);
        } catch (Exception e) {
            throw new VocabHunterException(String.format("Unable to read filter file '%s'", file), e);
        }
    }
}

