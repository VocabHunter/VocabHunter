/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class ExcelGridReaderImpl implements ExcelGridReader {
    @Override
    public List<GridLine> readGrid(final Path file, final Predicate<String> filter) {
        try (Workbook workbook = WorkbookFactory.create(file.toFile())) {
            Sheet sheet = workbook.getSheetAt(0);

            return StreamSupport.stream(sheet.spliterator(), false)
                .map(r -> row(r, filter))
                .collect(toList());
        } catch (IOException | InvalidFormatException e) {
            throw new VocabHunterException("Unable to read spreadsheet " + file, e);
        }
    }

    public GridLine row(final Row row, final Predicate<String> filter) {
        List<GridCell> cells = IntStream.range(0, row.getLastCellNum())
            .mapToObj(i -> row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL))
            .map(c -> cell(c, filter))
            .collect(toList());

        return new GridLine(cells);
    }

    private GridCell cell(final Cell original, final Predicate<String> filter) {
        String text;

        if (original == null) {
            text = "";
        } else {
            original.setCellType(CellType.STRING);

            text = original.getStringCellValue().trim();
        }

        return new GridCell(text, filter.test(text));
    }
}
