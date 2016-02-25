/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;

import java.nio.file.Path;

public class AnalysisTool {
    private static final int MIN_LETTERS = 2;

    private static final int MAX_WORDS = 100_000;

    private final FileStreamer fileStreamer;

    public AnalysisTool(final FileStreamer fileStreamer) {
        this.fileStreamer = fileStreamer;
    }

    public EnrichedSessionState createNewSession(final Path file) {
        return fileStreamer.createNewSession(file, MIN_LETTERS, MAX_WORDS);
    }

    public EnrichedSessionState createOrOpenSession(final Path file) {
        return fileStreamer.createOrOpenSession(file, MIN_LETTERS, MAX_WORDS);
    }
}
