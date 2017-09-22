/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineListToolTest {
    private static final List<String> LINES_A = listOf("A");

    private static final List<String> LINES_AB = listOf("A", "B");

    private static final List<String> LINES_BC = listOf("B", "C");

    private static final List<String> LINES_CD = listOf("C", "D");

    @Test
    public void testEmpty() {
        validate(emptyList());
    }

    @Test
    public void testEmptyContent() {
        validate(listOf(emptyList()));
    }

    @Test
    public void testSingle() {
        validate(listOf(LINES_A), "A");
    }

    @Test
    public void testSubset() {
        validate(listOf(LINES_A, LINES_AB), "A", "B");
    }

    @Test
    public void testIntersect() {
        validate(listOf(LINES_AB, LINES_BC), "A", "B", "C");
    }

    @Test
    public void testNoIntersect() {
        validate(listOf(LINES_AB, LINES_CD), "A", "B", "C", "D");
    }

    private void validate(final List<List<String>> input, final String... expected) {
        LineListTool<List<String>> target = new LineListTool<>(input, identity());
        List<String> expectedList = listOf(expected);

        assertEquals(expectedList, target.getLines(), "Lines");

        IntStream.range(0, expectedList.size())
            .forEach(n -> assertEquals(n, target.getLineNo(expectedList.get(n)), "Index"));
    }
}
