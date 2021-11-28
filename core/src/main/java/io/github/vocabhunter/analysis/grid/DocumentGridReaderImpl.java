/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.file.TextReader;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DocumentGridReaderImpl implements DocumentGridReader {
    private static final Pattern SPLITTER = Pattern.compile("\\r?\\n");

    private final TextReader textReader;

    @Inject
    public DocumentGridReaderImpl(final TextReader textReader) {
        this.textReader = textReader;
    }

    @Override
    public List<GridLine> readGrid(final Path file, final Predicate<String> filter) {
        String fullText = textReader.read(file);

        if ("".equals(fullText)) {
            return List.of();
        } else {
            return SPLITTER.splitAsStream(fullText)
                .map(String::trim)
                .map(s -> new GridCell(s, filter.test(s)))
                .map(GridLine::new)
                .toList();
        }
    }
}
