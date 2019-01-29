/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineListToolTest {
    private static final List<String> LINES_A = List.of("A");

    private static final List<String> LINES_AB = List.of("A", "B");

    private static final List<String> LINES_BC = List.of("B", "C");

    private static final List<String> LINES_CD = List.of("C", "D");

    @Test
    public void testEmpty() {
        validate(emptyList());
    }

    @Test
    public void testEmptyContent() {
        validate(List.of(List.of()));
    }

    @Test
    public void testSingle() {
        validate(List.of(LINES_A), "A");
    }

    @Test
    public void testSubset() {
        validate(List.of(LINES_A, LINES_AB), "A", "B");
    }

    @Test
    public void testIntersect() {
        validate(List.of(LINES_AB, LINES_BC), "A", "B", "C");
    }

    @Test
    public void testNoIntersect() {
        validate(List.of(LINES_AB, LINES_CD), "A", "B", "C", "D");
    }

    private void validate(final List<List<String>> input, final String... expected) {
        LineListTool<List<String>> target = new LineListTool<>(input, identity());
        List<String> expectedList = List.of(expected);

        assertEquals(expectedList, target.getLines(), "Lines");

        IntStream.range(0, expectedList.size())
            .forEach(n -> assertEquals(n, target.getLineNo(expectedList.get(n)), "Index"));
    }
}
