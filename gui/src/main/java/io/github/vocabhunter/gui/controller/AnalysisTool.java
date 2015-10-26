/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;

import java.nio.file.Path;

public final class AnalysisTool {
    private static final int MIN_LETTERS = 2;

    private static final int MAX_WORDS = 100_000;

    private AnalysisTool() {
        // Prevent instantiation - all methods are static
    }

    public static SessionState analyse(final Path file) {
        SimpleAnalyser analyser = new SimpleAnalyser();

        return FileStreamer.createNewSession(analyser, file, MIN_LETTERS, MAX_WORDS);
    }
}
