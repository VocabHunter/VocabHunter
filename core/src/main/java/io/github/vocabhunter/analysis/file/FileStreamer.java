/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.session.SessionState;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public final class FileStreamer {
    private FileStreamer() {
        // Prevent instantiation - all methods are static
    }

    public static Stream<String> stream(final InputStream in, final Path file) {
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

    private static List<String> splitToList(final String text) {
        List<String> list = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
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

    public static AnalysisResult analyse(final Analyser analyser, final Path file, final int minLetters, final int maxWords) {
        try (InputStream in = Files.newInputStream(file)) {
            Stream<String> stream = stream(in, file);

            return analyser.analyse(stream, file.getFileName().toString(), minLetters, maxWords);

        } catch (final IOException e) {
            throw readError(file, e);
        }
    }

    public static SessionState createNewSession(final Analyser analyser, final Path file, final int minLetters, final int maxWords) {
        AnalysisResult model = analyse(analyser, file, minLetters, maxWords);

        return new SessionState(model);
    }

    private static VocabHunterException readError(final Path file, final Exception e) {
        return new VocabHunterException(String.format("Unable to read file '%s'", file), e);
    }
}
