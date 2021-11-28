/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class SelectionExportTool {
    private static final Pattern SPLITTER = Pattern.compile("[\\t ]*\\n[\\t ]*");

    private SelectionExportTool() {
        // Prevent instantiation - all methods are static
    }

    public static void exportSelection(final SessionState state, final Path file, final boolean isNoteIncluded) {
        try {
            List<String> words = state.getOrderedUses().stream()
                .filter(sessionWord -> sessionWord.getState().equals(WordState.UNKNOWN))
                .flatMap(w -> wordLines(w, isNoteIncluded))
                .toList();

            Files.write(file, words);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to export to file '%s", file), e);
        }
    }

    private static Stream<String> wordLines(final SessionWord word, final boolean isNoteIncluded) {
        String note = word.getNote();
        String wordIdentifier = word.getWordIdentifier();

        if (isNoteIncluded && StringUtils.isNotBlank(note)) {
            String[] noteLines = SPLITTER.split(note.trim(), -1);

            return Stream.concat(
                Stream.of(wordIdentifier + '\t' + noteLines[0]),
                Stream.of(noteLines)
                    .skip(1)
                    .map(SelectionExportTool::noteLine)
            );
        } else {
            return Stream.of(wordIdentifier);
        }
    }

    private static String noteLine(final String text) {
        if ("".equals(text)) {
            return "";
        } else {
            return '\t' + text;
        }
    }
}
