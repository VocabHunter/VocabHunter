/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import javax.inject.Singleton;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;

@Singleton
public class SessionWordsToolImpl implements SessionWordsTool {
    @Override
    public List<String> knownWords(final Path file) {
        return guardRead(f -> SessionSerialiser.readWords(f, SessionWordsToolImpl::isKnown), file);
    }

    @Override
    public List<String> seenWords(final Path file) {
        return guardRead(f -> SessionSerialiser.readWords(f, SessionWordsToolImpl::isSeen), file);
    }

    private static boolean isKnown(final MarkedWord w) {
        return w.getState().equals(WordState.KNOWN);
    }

    private static boolean isSeen(final MarkedWord w) {
        WordState state = w.getState();

        return state.equals(WordState.KNOWN) || state.equals(WordState.UNKNOWN);
    }

    @Override
    public List<SessionWord> readMarkedWords(final Path file) {
        return guardRead(SessionSerialiser::readMarkedWords, file);
    }

    private <T> List<T> guardRead(final Function<Path, List<T>> reader, final Path file) {
        try {
            return reader.apply(file);
        } catch (final RuntimeException e) {
            throw new VocabHunterException(String.format("Unable to read filter file '%s'", filename(file)), e);
        }
    }
}
