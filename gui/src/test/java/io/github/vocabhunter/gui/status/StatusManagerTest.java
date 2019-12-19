/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import io.github.vocabhunter.gui.model.StatusModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusManagerTest {
    private static final String STATUS_SESSION_START = "Start a new VocabHunter session";

    private static final String STATUS_ABOUT = "About VocabHunter";

    private static final String STATUS_EXIT = "Exit VocabHunter";

    private final I18nManager i18nManager = I18nManagerImpl.createForDefaultLocale();

    private final StatusModel model = new StatusModel();

    private final PositionDescriptionTool positionDescriptionTool = new PositionDescriptionTool(i18nManager);

    private final StatusManagerImpl target = new StatusManagerImpl(i18nManager, positionDescriptionTool);

    @BeforeEach
    public void setUp() {
        target.setStatusModel(model);
    }

    @Test
    public void testBeginNewSession() {
        boolean isBeginNewSession = target.beginNewSession();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> validate(STATUS_SESSION_START, -1, true)
        );
    }

    @Test
    public void testCompleteNewSession() {
        boolean isBeginNewSession = target.beginNewSession();
        target.completeAction();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> validate("", 0, false)
        );
    }

    @Test
    public void testAttemptSecondActionFail() {
        boolean isBeginNewSession = target.beginNewSession();
        boolean isBeginAbout = target.beginAbout();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> assertFalse(isBeginAbout),
            () -> validate(STATUS_SESSION_START, -1, true)
        );
    }

    @Test
    public void testAttemptSecondActionSuccess() {
        boolean isBeginNewSession = target.beginNewSession();
        target.completeAction();
        boolean isBeginAbout = target.beginAbout();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> assertTrue(isBeginAbout),
            () -> validate(STATUS_ABOUT, -1, true)
        );
    }

    @Test
    public void testBeginExit() {
        boolean isBeginExit = target.beginExit();

        assertAll(
            () -> assertTrue(isBeginExit),
            () -> validate(STATUS_EXIT, -1, true)
        );
    }

    @Test
    public void testAttemptAboutAfterExit() {
        boolean isBeginExit = target.beginExit();
        boolean isBeginAbout = target.beginAbout();

        assertAll(
            () -> assertTrue(isBeginExit),
            () -> assertFalse(isBeginAbout),
            () -> validate(STATUS_EXIT, -1, true)
        );
    }

    @Test
    public void testBeginExitTwice() {
        boolean isBeginExit1 = target.beginExit();
        boolean isBeginExit2 = target.beginExit();

        assertAll(
            () -> assertTrue(isBeginExit1),
            () -> assertFalse(isBeginExit2),
            () -> validate(STATUS_EXIT, -1, true)
        );
    }

    @Test
    @Disabled
    public void testBeginNewSessionThenBeginExit() {
        boolean isBeginNewSession = target.beginNewSession();
        boolean isBeginExit = target.beginExit();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> assertTrue(isBeginExit),
            () -> validate(STATUS_EXIT, -1, true)
        );
    }

    @Test
    public void testCompleteExit() {
        boolean isBeginExit = target.beginExit();
        target.completeAction();

        assertAll(
            () -> assertTrue(isBeginExit),
            () -> validate("", 0, false)
        );
    }

    @Test
    @Disabled
    public void testCompleteExitAfterBeginNewSession() {
        boolean isBeginNewSession = target.beginNewSession();
        boolean isBeginExit = target.beginExit();
        target.completeAction();

        assertAll(
            () -> assertTrue(isBeginNewSession),
            () -> assertTrue(isBeginExit),
            () -> validate(STATUS_SESSION_START, -1, true)
        );
    }

    @Test
    public void testBeginExitAfterCompletingExit() {
        boolean isBeginExit1 = target.beginExit();
        target.completeAction();
        boolean isBeginExit2 = target.beginExit();

        assertAll(
            () -> assertTrue(isBeginExit1),
            () -> assertTrue(isBeginExit2),
            () -> validate(STATUS_EXIT, -1, true)
        );
    }

    private void validate(final String text, final double activity, final boolean busy) {
        assertAll(
            () -> assertEquals(text, model.getText()),
            () -> assertEquals(activity, model.getActivity()),
            () -> assertEquals(busy, model.isBusy())
        );
    }
}
