/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.analysis.settings.DocumentListedFile;
import io.github.vocabhunter.analysis.settings.ExcelListedFile;
import io.github.vocabhunter.analysis.settings.SessionListedFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.Collections.unmodifiableMap;

@Singleton
public class FilterFileWordsExtractorImpl implements FilterFileWordsExtractor {
    private static final Set<Integer> FIRST_COLUMN = Collections.singleton(0);

    private final Map<Class<?>, Function<BaseListedFile, List<String>>> extractors = buildExtractorMap();

    private final SessionWordsTool sessionWordsTool;

    private final GridWordsExtractor gridWordsExtractor;

    private final TextGridManager textGridManager;

    @Inject
    public FilterFileWordsExtractorImpl(final SessionWordsTool sessionWordsTool, final GridWordsExtractor gridWordsExtractor, final TextGridManager textGridManager) {
        this.sessionWordsTool = sessionWordsTool;
        this.gridWordsExtractor = gridWordsExtractor;
        this.textGridManager = textGridManager;
    }

    @Override
    public List<String> extract(final BaseListedFile file) {
        Function<BaseListedFile, List<String>> extractor = extractors.get(file.getClass());

        if (extractor == null) {
            throw new VocabHunterException("Unknown file type " + file);
        } else {
            return extractor.apply(file);
        }
    }

    private List<String> extractSessionListedFile(final BaseListedFile baseListedFile) {
        SessionListedFile file = (SessionListedFile) baseListedFile;

        if (file.isIncludeUnknown()) {
            return sessionWordsTool.seenWords(file.getFile());
        } else {
            return sessionWordsTool.knownWords(file.getFile());
        }
    }

    private List<String> extractExcelListedFile(final BaseListedFile baseListedFile) {
        ExcelListedFile file = (ExcelListedFile) baseListedFile;
        TextGrid grid = textGridManager.readExcel(file.getFile());

        return gridWordsExtractor.words(grid.getLines(), file.getColumns());
    }

    private List<String> extractDocumentListedFile(final BaseListedFile baseListedFile) {
        DocumentListedFile file = (DocumentListedFile) baseListedFile;
        TextGrid grid = textGridManager.readDocument(file.getFile());

        return gridWordsExtractor.words(grid.getLines(), FIRST_COLUMN);
    }

    private Map<Class<?>, Function<BaseListedFile, List<String>>> buildExtractorMap() {
        Map<Class<?>, Function<BaseListedFile, List<String>>> map = new ConcurrentHashMap<>();

        map.put(SessionListedFile.class, this::extractSessionListedFile);
        map.put(ExcelListedFile.class, this::extractExcelListedFile);
        map.put(DocumentListedFile.class, this::extractDocumentListedFile);

        return unmodifiableMap(map);
    }
}
