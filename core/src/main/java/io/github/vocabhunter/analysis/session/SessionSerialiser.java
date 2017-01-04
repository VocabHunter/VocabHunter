/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.FileTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.simple.WordStreamTool;

import java.nio.file.Path;
import java.util.List;

import static io.github.vocabhunter.analysis.session.SessionFormatVersion.*;
import static java.util.stream.Collectors.toList;

public final class SessionSerialiser {
    private SessionSerialiser() {
        // Prevent instantiation - all methods are static
    }

    public static void write(final Path file, final SessionState state) {
        FileTool.writeAsJson(file, state, "Unable to save file '%s'");
    }

    public static List<? extends MarkedWord> readMarkedWords(final Path file) {
        return readForMarkedWords(file).getOrderedUses();
    }

    public static EnrichedSessionState read(final Path file) {
        return new EnrichedSessionState(readInternal(file), file);
    }

    private static SessionState readInternal(final Path file) {
        return upgradeVersionStrict(readForMarkedWords(file));
    }

    private static SessionState readForMarkedWords(final Path file) {
        SessionState state = FileTool.readFromJson(SessionState.class, file, "Unable to load file '%s'");
        int formatVersion = state.getFormatVersion();

        if (formatVersion < 1 || formatVersion > LATEST_VERSION) {
            throw new VocabHunterException("This file was created with a newer version of VocabHunter.  Please upgrade and try again.");
        } else {
            return upgradeForMarkedWords(state);
        }
    }

    private static SessionState upgradeForMarkedWords(final SessionState original) {
        int version = original.getFormatVersion();

        if (version == FORMAT_1 || version == FORMAT_2) {
            return upgradeVersion1And2(original);
        } else {
            return original;
        }
    }

    private static SessionState upgradeVersion1And2(final SessionState original) {
        SessionState state = new SessionState();
        List<SessionWord> words = original.getOrderedUses().stream()
            .map(SessionSerialiser::upgradeVersion1And2)
            .sorted(WordStreamTool.WORD_COMPARATOR)
            .collect(toList());

        state.setFormatVersion(FORMAT_3);
        state.setName(original.getName());
        state.setOrderedUses(words);

        return state;
    }

    private static SessionWord upgradeVersion1And2(final SessionWord original) {
        SessionWord word = new SessionWord();
        String identifier = original.getWordIdentifier();
        List<String> uses = original.getUses();

        word.setWordIdentifier(identifier(identifier, uses));
        word.setUses(uses);
        word.setUseCount(countWords(identifier, uses));
        word.setState(original.getState());

        return word;
    }

    private static SessionState upgradeVersionStrict(final SessionState original) {
        int version = original.getFormatVersion();

        if (version == FORMAT_3) {
            return upgradeVersion3(original);
        } else {
            return original;
        }
    }

    private static SessionState upgradeVersion3(final SessionState original) {
        SessionState state = new SessionState();
        LineListTool<SessionWord> tool = new LineListTool<>(original.getOrderedUses(), SessionWord::getUses);
        List<SessionWord> words = original.getOrderedUses().stream()
            .map(w -> upgradeVersion3(tool, w))
            .collect(toList());

        state.setFormatVersion(FORMAT_4);
        state.setName(original.getName());
        state.setLines(tool.getLines());
        state.setOrderedUses(words);

        return state;
    }

    private static SessionWord upgradeVersion3(final LineListTool<SessionWord> tool, final SessionWord original) {
        List<Integer> lineNos = original.getUses().stream()
            .map(tool::getLineNo)
            .collect(toList());
        SessionWord word = new SessionWord();

        word.setWordIdentifier(original.getWordIdentifier());
        word.setState(original.getState());
        word.setUseCount(original.getUseCount());
        word.setLineNos(lineNos);

        return word;
    }

    private static int countWords(final String identifier, final List<String> uses) {
        return (int) uses.stream()
            .flatMap(WordStreamTool::words)
            .filter(w -> w.equalsIgnoreCase(identifier))
            .count();
    }

    private static String identifier(final String oldIdentifier, final List<String> uses) {
        return uses.stream()
            .flatMap(WordStreamTool::words)
            .filter(w -> w.equalsIgnoreCase(oldIdentifier))
            .reduce(WordStreamTool::preferredForm)
            .orElse(oldIdentifier);
    }
}
