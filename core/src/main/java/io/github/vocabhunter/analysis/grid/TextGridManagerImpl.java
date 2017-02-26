/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class TextGridManagerImpl implements TextGridManager {
    private static final Pattern FILTER_PATTERN = Pattern.compile("[^\\s]+\\s+[^\\s]+");

    public static final Predicate<String> FILTER = FILTER_PATTERN.asPredicate();

    private final DocumentGridReader documentGridReader;

    private final ExcelGridReader excelGridReader;

    private final TextGridBuilder textGridBuilder;

    @Inject
    public TextGridManagerImpl(final DocumentGridReader documentGridReader, final ExcelGridReader excelGridReader, final TextGridBuilder textGridBuilder) {
        this.documentGridReader = documentGridReader;
        this.excelGridReader = excelGridReader;
        this.textGridBuilder = textGridBuilder;
    }

    @Override
    public TextGrid readDocument(final Path file) {
        List<GridLine> lines = documentGridReader.readGrid(file, FILTER);

        return textGridBuilder.build(lines);
    }

    @Override
    public TextGrid readExcel(final Path file) {
        List<GridLine> lines = excelGridReader.readGrid(file, FILTER);

        return textGridBuilder.build(lines);
    }
}
