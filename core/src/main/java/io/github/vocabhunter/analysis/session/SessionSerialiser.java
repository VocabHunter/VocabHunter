/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.simple.WordStreamTool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.vocabhunter.analysis.session.SessionFormatVersion.*;

public final class SessionSerialiser {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SessionSerialiser() {
        // Prevent instantiation - all methods are static
    }

    public static void write(final Path file, final SessionState state) {
        try {
            MAPPER.writeValue(file.toFile(), state);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to save file '%s'", file), e);
        }
    }

    public static EnrichedSessionState read(final Path file) {
        try {
            SessionState state = MAPPER.readValue(file.toFile(), SessionState.class);
            int formatVersion = state.getFormatVersion();

            if (formatVersion < 1 || formatVersion > LATEST_VERSION) {
                throw new VocabHunterException("This file was created with a newer version of VocabHunter.  Please upgrade and try again.");
            } else {
                return new EnrichedSessionState(upgradeVersion(state), file);
            }
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load file '%s'", file), e);
        }
    }

    private static SessionState upgradeVersion(final SessionState original) {
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
            .collect(Collectors.toList());

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
