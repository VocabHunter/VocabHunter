/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleAnalyserTest {
    private enum LineReference {
        FIRST(0), SECOND(1);

        private final int lineNo;

        LineReference(final int lineNo) {
            this.lineNo = lineNo;
        }

        public int getLineNo() {
            return lineNo;
        }
    }

    private static final String NAME = "Name";

    private static final String WORD_1 = "one";

    private static final String WORD_2 = "two";

    private static final String WORD_3 = "three";

    private static final String LINE_1 = "One two two one.";

    private static final String LINE_2 = "Three one three.";

    private static final String LINE_WITH_SPACES = " one    two . three  ";

    private static final String SPANISH_1 = "actuaci\u00F3n";

    private static final String SPANISH_2 = "de";

    private static final String SPANISH_3 = "ping\u00FCinos";

    private static final String SPANISH_4 = "espa\u00F1oles";

    private static final String UPPER_CASE = "Word";

    private static final String LOWER_CASE = "word";

    private static final String UPPER_UPPER = "Word Word";

    private static final String UPPER_LOWER = "Word word";

    private static final String LOWER_LOWER = "word word";

    private static final String LINE_WITH_ACCENTS = String.join(" ", SPANISH_1, SPANISH_2, SPANISH_3, SPANISH_4);

    private final Analyser target = new SimpleAnalyser();

    @Test
    public void testName() {
        AnalysisResult result = analyse();

        assertEquals("Name", NAME, result.getName());
    }

    @Test
    public void testEmpty() {
        AnalysisResult result = analyse();

        assertTrue("Uses", result.getOrderedUses().isEmpty());
    }

    @Test
    public void testSingle() {
        AnalysisResult result = analyse(WORD_1);

        validate(result, use(WORD_1, 1, LineReference.FIRST));
    }

    @Test
    public void testMulti() {
        AnalysisResult result = analyse(LINE_1, LINE_2);

        validate(result, use(WORD_1, 3, LineReference.FIRST, LineReference.SECOND), use(WORD_3, 2, LineReference.SECOND), use(WORD_2, 2, LineReference.FIRST));
    }

    @Test
    public void testExtraSpaces() {
        AnalysisResult result = analyse(LINE_WITH_SPACES);

        validate(result, use(WORD_1, 1, LineReference.FIRST), use(WORD_3, 1, LineReference.FIRST), use(WORD_2, 1, LineReference.FIRST));
    }

    @Test
    public void testSpanish() {
        AnalysisResult result = analyse(LINE_WITH_ACCENTS);

        validate(result, use(SPANISH_1, 1, LineReference.FIRST), use(SPANISH_2, 1, LineReference.FIRST), use(SPANISH_4, 1, LineReference.FIRST), use(SPANISH_3, 1, LineReference.FIRST));
    }

    @Test
    public void testMixedCase() {
        AnalysisResult result = analyse(UPPER_CASE, LOWER_CASE);

        validate(result, use(LOWER_CASE, 2, LineReference.FIRST, LineReference.SECOND));
    }

    @Test
    public void testLowerCase() {
        AnalysisResult result = analyse(LOWER_CASE, LOWER_CASE);

        validate(result, use(LOWER_CASE, 2, LineReference.FIRST, LineReference.SECOND));
    }

    @Test
    public void testUpperCase() {
        AnalysisResult result = analyse(UPPER_CASE, UPPER_CASE);

        validate(result, use(UPPER_CASE, 2, LineReference.FIRST, LineReference.SECOND));
    }

    @Test
    public void testUpperUpper() {
        AnalysisResult result = analyse(UPPER_UPPER);

        validate(result, use(UPPER_CASE, 2, LineReference.FIRST));
    }

    @Test
    public void testUpperLower() {
        AnalysisResult result = analyse(UPPER_LOWER);

        validate(result, use(LOWER_CASE, 2, LineReference.FIRST));
    }

    @Test
    public void testLowerLower() {
        AnalysisResult result = analyse(LOWER_LOWER);

        validate(result, use(LOWER_CASE, 2, LineReference.FIRST));
    }

    private AnalysisResult analyse(final String... lines) {
        return target.analyse(listOf(lines), NAME);
    }

    private WordUse use(final String wordIdentifier, final int useCount, final LineReference... lines) {
        List<Integer> lineNos = Stream.of(lines)
            .map(LineReference::getLineNo)
            .collect(Collectors.toList());

        return new WordUse(wordIdentifier, useCount, lineNos);
    }

    private void validate(final AnalysisResult result, final WordUse... expected) {
        List<WordUse> expectedList = listOf(expected);

        assertEquals("Uses", expectedList, result.getOrderedUses());
    }
}
