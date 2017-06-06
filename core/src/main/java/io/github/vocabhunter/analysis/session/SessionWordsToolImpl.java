/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.inject.Singleton;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;
import static java.util.stream.Collectors.toList;

@Singleton
public class SessionWordsToolImpl implements SessionWordsTool {
    private static final Logger LOG = LoggerFactory.getLogger(SessionWordsToolImpl.class);

    @Override
    public List<String> knownWords(final Path file) {
        return timedCollect("known", () -> knownWords(readMarkedWords(file)), file);
    }

    private static List<String> knownWords(final List<? extends MarkedWord> words) {
        return words(words, SessionWordsToolImpl::isKnown);
    }

    @Override
    public List<String> seenWords(final Path file) {
        return timedCollect("known or unknown", () -> seenWords(readMarkedWords(file)), file);
    }

    private static List<String> seenWords(final List<? extends MarkedWord> words) {
        return words(words, SessionWordsToolImpl::isSeen);
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

    @Override
    public List<? extends MarkedWord> readMarkedWords(final Path file) {
        try {
            return SessionSerialiser.readMarkedWords(file);
        } catch (final Exception e) {
            throw new VocabHunterException(String.format("Unable to read filter file '%s'", filename(file)), e);
        }
    }

    private static <T extends List<?>> T timedCollect(final String type, final Supplier<T> s, final Path file) {
        Instant start = Instant.now();
        T words = s.get();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        String filename = filename(file);

        LOG.info("Read filter file and found {} words marked as {} in {}ms ({})", words.size(), type, duration.toMillis(), filename);

        return words;
    }
}
