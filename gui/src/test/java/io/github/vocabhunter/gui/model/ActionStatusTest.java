/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionStatusTest {
    @ParameterizedTest
    @CsvSource({
        "MAIN,                SECONDARY",
        "SECONDARY,                    ",
        "EXIT_FROM_MAIN,               ",
        "EXIT_FROM_SECONDARY,          "
    })
    public void testBeginSecondary(final ActionStatus start, final ActionStatus end) {
        Optional<ActionStatus> actual = start.beginSecondary();

        assertEquals(Optional.ofNullable(end), actual);
    }

    @ParameterizedTest
    @CsvSource({
        "MAIN,                EXIT_FROM_MAIN     ",
        "SECONDARY,           EXIT_FROM_SECONDARY",
        "EXIT_FROM_MAIN,                         ",
        "EXIT_FROM_SECONDARY,                    "
    })
    public void testBeginExit(final ActionStatus start, final ActionStatus end) {
        Optional<ActionStatus> actual = start.beginExit();

        assertEquals(Optional.ofNullable(end), actual);
    }

    @ParameterizedTest
    @CsvSource({
        "MAIN,                         ",
        "SECONDARY,           MAIN     ",
        "EXIT_FROM_MAIN,      MAIN     ",
        "EXIT_FROM_SECONDARY, SECONDARY"
    })
    public void testComplete(final ActionStatus start, final ActionStatus end) {
        Optional<ActionStatus> actual = start.complete();

        assertEquals(Optional.ofNullable(end), actual);
    }
}
