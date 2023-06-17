/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.analysis.session.SessionState;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

@Singleton
public class FileStreamer {
    private static final Logger LOG = LoggerFactory.getLogger(FileStreamer.class);

    private final TextReader textReader;

    private final Analyser analyser;

    @Inject
    public FileStreamer(final TextReader textReader, final Analyser analyser) {
        this.textReader = textReader;
        this.analyser = analyser;
    }

    public AnalysisResult analyse(final Path file) {
        Instant start = Instant.now();
        String fullText = readText(file);
        String filename = FileNameTool.filename(file);
        AnalysisResult result = analyser.analyse(fullText, filename);
        int count = result.getOrderedUses().size();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        LOG.info("Analysed text and found {} words in {}ms ({})", count, duration.toMillis(), filename);

        return result;
    }

    public EnrichedSessionState createNewSession(final Path file) {
        AnalysisResult model = analyse(file);

        return new EnrichedSessionState(new SessionState(model));
    }

    public EnrichedSessionState createOrOpenSession(final Path file) {
        if (FileNameTool.isSessionFile(file)) {
            return SessionSerialiser.read(file);
        } else {
            return createNewSession(file);
        }
    }

    private String readText(final Path file) {
        String fullText = textReader.read(file);

        if (StringUtils.isBlank(fullText)) {
            throw new VocabHunterException(String.format("No text in file '%s'", file));
        } else {
            return fullText;
        }
    }
}
