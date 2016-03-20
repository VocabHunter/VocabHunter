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
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreConstants.LOCALE;

public class FileStreamer {
    private static final Logger LOG = LoggerFactory.getLogger(FileStreamer.class);

    private final Analyser analyser;

    public FileStreamer(final Analyser analyser) {
        this.analyser = analyser;
    }

    public Stream<String> stream(final InputStream in, final Path file) {
        try {
            ContentHandler textHandler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            ParseContext context = new ParseContext();

            parser.parse(in, textHandler, metadata, context);

            String fullText = textHandler.toString();

            if (StringUtils.isBlank(fullText)) {
                throw new VocabHunterException(String.format("No text in file '%s'", file));
            } else {
                return splitToList(fullText).stream();
            }
        } catch (IOException | SAXException | TikaException e) {
            throw readError(file, e);
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
                list.add(line.replaceAll("\\s+", " ").trim());
            }
            start = end;
            end = iterator.next();
        }

        return list;
    }

    public AnalysisResult analyse(final Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            Stream<String> stream = stream(in, file);

            return analyser.analyse(stream, FileNameTool.filename(file));

        } catch (final IOException e) {
            throw readError(file, e);
        }
    }

    public EnrichedSessionState createNewSession(final Path file) {
        AnalysisResult model = analyse(file);

        return new EnrichedSessionState(new SessionState(model));
    }

    public EnrichedSessionState createOrOpenSession(final Path file) {
        try {
            return SessionSerialiser.read(file);
        } catch (final VocabHunterException e) {
            LOG.debug("{} is not a session file", file, e);
            return createNewSession(file);
        }
    }

    private VocabHunterException readError(final Path file, final Exception e) {
        return new VocabHunterException(String.format("Unable to read file '%s'", file), e);
    }
}
