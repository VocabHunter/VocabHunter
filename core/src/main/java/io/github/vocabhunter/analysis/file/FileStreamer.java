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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.BreakIterator;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.analysis.core.CoreConstants.LOCALE;

@Singleton
public class FileStreamer {
    private static final Logger LOG = LoggerFactory.getLogger(FileStreamer.class);

    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    private final Analyser analyser;

    @Inject
    public FileStreamer(final Analyser analyser) {
        this.analyser = analyser;
    }

    public List<String> lines(final Path file) {
        String fullText = TikaTool.read(file);

        if (StringUtils.isBlank(fullText)) {
            throw new VocabHunterException(String.format("No text in file '%s'", file));
        } else {
            return splitToList(fullText);
        }
    }

    private List<String> splitToList(final String text) {
        List<String> list = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(LOCALE);
        iterator.setText(text);
        int start = iterator.first();
        int end = iterator.next();

        while (end != BreakIterator.DONE) {
            String line = text.substring(start, end);

            if (StringUtils.isNoneBlank(line)) {
                list.add(SPACE_PATTERN.matcher(line).replaceAll(" ").trim());
            }
            start = end;
            end = iterator.next();
        }

        return list;
    }

    public AnalysisResult analyse(final Path file) {
        Instant start = Instant.now();
        List<String> stream = lines(file);
        String filename = FileNameTool.filename(file);
        AnalysisResult result = analyser.analyse(stream, filename);
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
}
