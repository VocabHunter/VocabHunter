/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.marked.WordState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.analysis.marked.WordState.*;
import static java.util.stream.Collectors.toList;

public final class TestSessionStateTool {
    private static final String DOCUMENT_NAME = "test-sample.txt";

    public static final String LINE_1 = "The quick brown fox jumped over the lazy dog's back.";

    public static final String LINE_2 = "Now is the time for all good men to come to the aid of the party.";

    public static final String LINE_3 = "This is a simple test document.";

    private static final List<String> LINES = listOf(LINE_1, LINE_2, LINE_3);

    private TestSessionStateTool() {
        // Prevent instantiation - all methods are static
    }

    public static SessionState buildSession() {
        return buildSession(LINES);
    }

    public static SessionState buildSession(final List<String> lines) {
        SessionState state = new SessionState();

        state.setName(DOCUMENT_NAME);
        List<SessionWord> uses = new ArrayList<>();

        use(uses, "the", KNOWN, 5, lines, LINE_1, LINE_2);
        use(uses, "is", UNKNOWN, 2, lines, LINE_2, LINE_3);
        use(uses, "to", KNOWN, 2, lines, LINE_2);
        use(uses, "a", UNKNOWN, 1, lines, LINE_3);
        use(uses, "aid", UNSEEN, 1, lines, LINE_2);
        use(uses, "all", UNSEEN, 1, lines, LINE_2);
        use(uses, "back", UNSEEN, 1, lines, LINE_1);
        use(uses, "brown", UNSEEN, 1, lines, LINE_1);
        use(uses, "come", UNSEEN, 1, lines, LINE_2);
        use(uses, "document", UNSEEN, 1, lines, LINE_3);
        use(uses, "dog's", UNSEEN, 1, lines, LINE_1);
        use(uses, "for", UNSEEN, 1, lines, LINE_2);
        use(uses, "fox", UNSEEN, 1, lines, LINE_1);
        use(uses, "good", UNSEEN, 1, lines, LINE_2);
        use(uses, "jumped", UNSEEN, 1, lines, LINE_1);
        use(uses, "lazy", UNSEEN, 1, lines, LINE_1);
        use(uses, "men", UNSEEN, 1, lines, LINE_2);
        use(uses, "Now", UNSEEN, 1, lines, LINE_2);
        use(uses, "of", UNSEEN, 1, lines, LINE_2);
        use(uses, "over", UNSEEN, 1, lines, LINE_1);
        use(uses, "party", UNSEEN, 1, lines, LINE_2);
        use(uses, "quick", UNSEEN, 1, lines, LINE_1);
        use(uses, "simple", UNSEEN, 1, lines, LINE_3);
        use(uses, "test", UNSEEN, 1, lines, LINE_3);
        use(uses, "This", UNSEEN, 1, lines, LINE_3);
        use(uses, "time", UNSEEN, 1, lines, LINE_2);

        state.setOrderedUses(uses);
        state.setLines(lines);

        return state;
    }

    private static void use(final List<SessionWord> uses, final String word, final WordState state, final int useCount, final List<String> lines, final String... usedLines) {
        SessionWord sw = new SessionWord();
        List<Integer> lineNos = Stream.of(usedLines)
            .map(lines::indexOf)
            .collect(toList());

        sw.setWordIdentifier(word);
        sw.setState(state);
        sw.setLineNos(lineNos);
        sw.setUseCount(useCount);

        uses.add(sw);
    }
}
