/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vocabhunter.analysis.core.VocabHunterException;

import java.io.IOException;
import java.nio.file.Path;

public final class SessionSerialiser {
    private static final int MAX_SUPPORTED_VERSION = 1;

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

            if (formatVersion < 1 || formatVersion > MAX_SUPPORTED_VERSION) {
                throw new VocabHunterException("This file was created with a newer version of VocabHunter.  Please upgrade and try again.");
            } else {
                EnrichedSessionState enriched = new EnrichedSessionState(state, file);

                return enriched;
            }
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load file '%s'", file), e);
        }
    }
}
