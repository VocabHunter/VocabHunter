/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.file.SelectionExportTool;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.analysis.session.SessionState;

import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionFileService {
    private final FileStreamer streamer;

    @Inject
    public SessionFileService(final FileStreamer streamer) {
        this.streamer = streamer;
    }

    public EnrichedSessionState createNewSession(final Path file) {
        return streamer.createNewSession(file);
    }

    public EnrichedSessionState createOrOpenSession(final Path file) {
        return streamer.createOrOpenSession(file);
    }

    public EnrichedSessionState read(final Path file) {
        return SessionSerialiser.read(file);
    }

    public void write(final Path file, final SessionState state) {
        SessionSerialiser.write(file, state);
    }

    public void exportSelection(final SessionState state, final Path file) {
        SelectionExportTool.exportSelection(state, file);
    }
}
