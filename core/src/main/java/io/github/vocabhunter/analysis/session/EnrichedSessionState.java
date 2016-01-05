/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import java.nio.file.Path;
import java.util.Optional;

public class EnrichedSessionState {
    private final SessionState state;

    private final Path file;

    public EnrichedSessionState(final SessionState state) {
        this(state, null);
    }

    public EnrichedSessionState(final SessionState state, final Path file) {
        this.state = state;
        this.file = file;
    }

    public SessionState getState() {
        return state;
    }

    public Optional<Path> getFile() {
        return Optional.ofNullable(file);
    }
}
