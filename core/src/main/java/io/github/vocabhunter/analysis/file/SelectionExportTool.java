/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class SelectionExportTool {
    private SelectionExportTool() {
        // Prevent instantiation - all methods are static
    }

    public static void exportSelection(final SessionState state, final Path file) {
        try {
            List<String> words = state.getOrderedUses().stream()
                    .filter(sessionWord -> sessionWord.getState().equals(WordState.UNKNOWN))
                    .map(SessionWord::getWordIdentifier)
                    .collect(Collectors.toList());

            Files.write(file, words);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to export to file '%s", file), e);
        }
    }
}
