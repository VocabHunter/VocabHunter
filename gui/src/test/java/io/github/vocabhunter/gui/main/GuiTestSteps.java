/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;

import static io.github.vocabhunter.gui.common.GuiConstants.*;
import static io.github.vocabhunter.gui.main.GuiTestConstants.BOOK1;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

public class GuiTestSteps {
    private static final Logger LOG = LoggerFactory.getLogger(GuiTestSteps.class);

    private final FxRobot robot;

    private final GuiTestValidator validator;

    private int stepNo;

    public GuiTestSteps(final FxRobot robot, final GuiTestValidator validator) {
        this.robot = robot;
        this.validator = validator;
    }

    public void part1BasicWalkThrough() {
        step("Open application", () -> {
            verifyThat("#mainBorderPane", isVisible());
        });

        step("Start new session", () -> {
            robot.clickOn("#buttonNew");
            verifyThat("#mainWordPane", isVisible());
            verifyThat("#mainWord", hasText("and"));
        });

        step("Mark word as known", () -> {
            robot.clickOn("#buttonKnown");
            verifyThat("#mainWord", hasText("the"));
        });

        step("Mark word as unknown", () -> {
            robot.clickOn("#buttonUnknown");
            verifyThat("#mainWord", hasText("to"));
        });

        step("Show selection", () -> {
            robot.clickOn("#buttonEditOff");
            verifyThat("#buttonKnown", isInvisible());
        });

        step("Return to edit mode", () -> {
            robot.clickOn("#buttonEditOn");
            verifyThat("#buttonKnown", isVisible());
        });

        step("Export the selection", () -> {
            robot.clickOn("#buttonExport");
            validator.validateExportFile();
        });

        step("Save the session", () -> {
            robot.clickOn("#buttonSave");
            validator.validateSavedSession(BOOK1);
        });
    }

    public void part2Progress() {
        step("Check progress", () -> {
            robot.clickOn("#tabProgress");
            verifyThat("#labelValueDone", hasText("2 Words"));
        });

        step("Return to analysis", () -> {
            robot.clickOn("#tabAnalysis");
            verifyThat("#mainWord", hasText("the"));
        });

    }

    public void part3StartNewSessionAndFilter() {
        step("Open a new session for a different book", () -> {
            robot.clickOn("#buttonNew");
            verifyThat("#mainWord", hasText("the"));
        });

        step("Define filter", () -> {
            robot.clickOn("#buttonSetupFilters");
            robot.doubleClickOn("#fieldMinimumLetters").write("6");
            robot.doubleClickOn("#fieldMinimumOccurrences").write("4");
            robot.clickOn("#fieldInitialCapital");
            robot.clickOn("#buttonOk");
            verifyThat("#mainWord", hasText("surgeon"));
        });

        step("Mark filtered word as known", () -> {
            robot.clickOn("#buttonKnown");
            verifyThat("#mainWord", hasText("workhouse"));
        });

        step("Disable filter", () -> {
            robot.clickOn("#buttonEnableFilters");
            verifyThat("#mainWord", hasText("workhouse"));
        });
    }

    public void part4ReopenFirstSession() {
        step("Re-open the old session", () -> {
            robot.clickOn("#buttonOpen");
            robot.clickOn("Discard");
            verifyThat("#mainWord", hasText("a"));
        });
    }

    public void part5AboutDialogue() {
        step("Open About dialogue", () -> {
            robot.clickOn("#menuHelp");
            robot.clickOn("#menuAbout");
            verifyThat("#aboutDialogue", isVisible());
        });

        step("Open website from About dialogue", () -> {
            robot.clickOn("#linkWebsite");
            validator.validateWebPage(WEBSITE);
        });

        step("Close About dialogue", () -> {
            robot.clickOn("#buttonClose");
        });
    }

    public void part6WebLinks() {
        step("Open website", () -> {
            robot.clickOn("#menuHelp");
            robot.clickOn("#menuWebsite");
            validator.validateWebPage(WEBSITE);
        });

        step("Open VocabHunter How To", () -> {
            robot.clickOn("#menuHelp");
            robot.clickOn("#menuHowTo");
            validator.validateWebPage(WEBPAGE_HELP);
        });

        step("Open Issue Reporting", () -> {
            robot.clickOn("#menuHelp");
            robot.clickOn("#menuIssue");
            validator.validateWebPage(WEBPAGE_ISSUE);
        });
    }

    public void part7Search() {
        step("Open Search", () -> {
            robot.clickOn("#menuWords");
            robot.clickOn("#menuFind");
            verifyThat("#barSearch", isVisible());
        });
        step("Enter search word", () -> {
            robot.doubleClickOn("#fieldSearch").write("try");
            verifyThat("#mainWord", hasText("country"));
            verifyThat("#labelMatches", hasText("1 of 2 matches"));
        });
        step("Select next match", () -> {
            robot.clickOn("#buttonSearchDown");
            verifyThat("#mainWord", hasText("trying"));
            verifyThat("#labelMatches", hasText("2 of 2 matches"));
        });
        step("Select previous match", () -> {
            robot.clickOn("#buttonSearchUp");
            verifyThat("#mainWord", hasText("country"));
            verifyThat("#labelMatches", hasText("1 of 2 matches"));
        });
        step("Seach with no match", () -> {
            robot.doubleClickOn("#fieldSearch").write("bananas");
            verifyThat("#mainWord", hasText("back"));
            verifyThat("#labelMatches", hasText("No matches"));
        });
        step("Close Search", () -> {
            robot.clickOn("#buttonCloseSearch");
            verifyThat("#barSearch", isInvisible());
        });
    }

    private void step(final String step, final Runnable runnable) {
        ++stepNo;
        LOG.info("STEP {}: Begin - {}", stepNo, step);
        runnable.run();
        LOG.info("STEP {}:   End - {}", stepNo, step);
    }
}
