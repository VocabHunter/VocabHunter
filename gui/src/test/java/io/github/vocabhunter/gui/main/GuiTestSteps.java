/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.test.utils.TestFileManager;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;
import org.testfx.service.query.NodeQuery;

import java.nio.file.Path;

import static io.github.vocabhunter.gui.common.GuiConstants.*;
import static io.github.vocabhunter.gui.main.GuiTestConstants.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

public class GuiTestSteps {
    private static final Logger LOG = LoggerFactory.getLogger(GuiTestSteps.class);

    private static final String WORD_NOTE_1 = "Typed and then discarded";

    private static final String WORD_NOTE_2 = "A comment made about this word.";

    private final FxRobot robot;

    private final GuiTestValidator validator;

    private final Path exportFile;

    private final Path sessionFile;

    private int stepNo;

    public GuiTestSteps(final FxRobot robot, final GuiTestValidator validator, final TestFileManager manager) {
        this.robot = robot;
        this.validator = validator;

        exportFile = manager.addFile("export.txt");
        sessionFile = manager.addFile("session.wordy");
    }

    public void part1BasicWalkThrough() {
        step("Open application", () -> {
            verifyThat("#mainBorderPane", isVisible());
        });

        step("Start new session", () -> {
            validator.setUpFileDialogue(FileDialogueType.NEW_SESSION, FileFormatType.DOCUMENT, BOOK_1);
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

        step("Mark word as known with keyboard", () -> {
            robot.type(KeyCode.K);
            verifyThat("#mainWord", hasText("me"));
        });

        step("Mark word as unknown with keyboard", () -> {
            robot.type(KeyCode.X);
            verifyThat("#mainWord", hasText("of"));
        });

        step("Write note and cancel", () -> {
            robot.clickOn("#buttonNote");
            robot.clickOn("#textAreaNoteText").write(WORD_NOTE_1);
            robot.clickOn("#buttonCancel");
            verifyThat("#textAreaNotePreview", isInvisible());
        });

        step("Write note and add to word", () -> {
            robot.clickOn("#buttonNote");
            robot.clickOn("#textAreaNoteText").write(WORD_NOTE_2);
            robot.clickOn("#buttonOk");
            verifyThat("#textAreaNotePreview", isVisible());
            verifyThat("#textAreaNotePreview", hasText(WORD_NOTE_2));
        });

        step("Open note with keyboard", () -> {
            robot.type(KeyCode.N);
            verifyThat("#textAreaNoteText", isVisible());
            robot.clickOn("#buttonOk");
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
            validator.setUpFileDialogue(FileDialogueType.EXPORT_SELECTION, FileFormatType.TEXT, exportFile);
            robot.clickOn("#buttonExport");
            validator.validateExportFile(exportFile);
        });

        step("Save the session", () -> {
            validator.setUpFileDialogue(FileDialogueType.SAVE_SESSION, FileFormatType.SESSION, sessionFile);
            robot.clickOn("#buttonSave");
            validator.validateSavedSession(sessionFile, BOOK_1);
        });
    }

    public void part2Progress() {
        step("Check progress", () -> {
            robot.clickOn("#tabProgress");
            verifyThat("#labelValueDone", hasText("4 Words"));
        });

        step("Return to analysis", () -> {
            robot.clickOn("#tabAnalysis");
            verifyThat("#mainWord", hasText("me"));
        });

    }

    public void part3StartNewSessionAndFilter() {
        step("Open a new session for a different book", () -> {
            validator.setUpFileDialogue(FileDialogueType.NEW_SESSION, FileFormatType.DOCUMENT, BOOK_2);
            robot.clickOn("#buttonNew");
            verifyThat("#mainWord", hasText("the"));
        });

        step("Define impossible filter", () -> {
            robot.clickOn("#buttonSetupFilters");
            robot.doubleClickOn("#fieldMinimumLetters").write("1000");
            robot.clickOn("#buttonOk");
            verifyThat("#filterErrorDialogue", isVisible());
        });

        step("Close filter error", () -> {
            robot.clickOn("OK");
        });

        step("Define filter", () -> {
            robot.clickOn("#buttonSetupFilters");
            robot.doubleClickOn("#fieldMinimumLetters").write("4");
            robot.doubleClickOn("#fieldMinimumOccurrences").write("3");
            robot.clickOn("#fieldInitialCapital");
            robot.clickOn("#buttonOk");
            verifyThat("#mainWord", hasText("have"));
        });

        step("Add empty session to filter", () -> {
            validator.setUpFileDialogue(FileDialogueType.OPEN_SESSION, FileFormatType.SESSION, SESSION_NO_MARKED_WORDS);
            robot.clickOn("#buttonSetupFilters");
            robot.clickOn("#buttonAddSessionFile");
            verifyThat("#buttonAddFilterFile", isDisabled());
        });

        step("Change session filter to file with marked words", () -> {
            validator.setUpFileDialogue(FileDialogueType.OPEN_SESSION, FileFormatType.SESSION, SESSION_1);
            robot.clickOn("#buttonChangeFile");
            robot.clickOn("#buttonAddFilterFile");
            robot.clickOn("#buttonEdit");
            verifyThat("#buttonAddFilterFile", isEnabled());
            verifyThat("#labelTotalWords", hasText("1"));
        });

        step("Include unknown words in filter", () -> {
            robot.clickOn("#buttonSeen");
            verifyThat("#buttonAddFilterFile", isEnabled());
            verifyThat("#labelTotalWords", hasText("3"));
        });

        step("Add session to filter", () -> {
            robot.clickOn("#buttonAddFilterFile");
            robot.clickOn("#buttonOk");
            verifyThat("#mainWord", hasText("that"));
        });

        step("Add empty word list to filter", () -> {
            validator.setUpFileDialogue(FileDialogueType.OPEN_WORD_LIST, FileFormatType.DOCUMENT, EMPTY_FILE);
            robot.clickOn("#buttonSetupFilters");
            robot.clickOn("#buttonAddGridFile");
            verifyThat("#buttonAddFilterFile", isDisabled());
        });

        step("Change filter file to spreadsheet", () -> {
            validator.setUpFileDialogue(FileDialogueType.OPEN_WORD_LIST, FileFormatType.SPREADSHEET, FILTER_SPREADSHEET);
            robot.clickOn("#buttonChangeFile");
            verifyThat("#buttonAddFilterFile", isEnabled());
            verifyThat("#labelTotalWords", hasText("1"));
        });

        step("Select second column of spreadsheet", () -> {
            robot.clickOn("#checkBoxColumn1");
            verifyThat("#buttonAddFilterFile", isEnabled());
            verifyThat("#labelTotalWords", hasText("3"));
        });

        step("Apply spreadsheet filter", () -> {
            robot.clickOn("#buttonAddFilterFile");
            robot.clickOn("#buttonOk");
            verifyThat("#mainWord", hasText("this"));
        });

        step("Mark filtered word as known", () -> {
            robot.clickOn("#buttonKnown");
            verifyThat("#mainWord", hasText("child"));
        });

        step("Disable filter", () -> {
            robot.clickOn("#buttonEnableFilters");
            verifyThat("#mainWord", hasText("child"));
        });
    }

    public void part4ReopenFirstSession() {
        step("Re-open the old session", () -> {
            validator.setUpFileDialogue(FileDialogueType.OPEN_SESSION, FileFormatType.SESSION, sessionFile);
            robot.clickOn("#buttonOpen");
            robot.clickOn("Discard");
            verifyThat("#mainWord", hasText("of"));
        });
    }

    public void part5ErrorHandling() {
        step("Start session from empty file", () -> {
            validator.setUpFileDialogue(FileDialogueType.NEW_SESSION, FileFormatType.DOCUMENT, EMPTY_FILE);
            robot.clickOn("#buttonNew");
            verifyThat("#errorDialogue", isVisible());
        });
        step("Close error dialogue", () -> {
            robot.clickOn("OK");
        });
    }

    public void part6AboutDialogue() {
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

    public void part7WebLinks() {
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

    public void part8Search() {
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
        step("Search with no match", () -> {
            robot.doubleClickOn("#fieldSearch").write("bananas");
            verifyThat("#mainWord", hasText("back"));
            verifyThat("#labelMatches", hasText("No matches"));
        });
        step("Close Search", () -> {
            robot.clickOn("#buttonCloseSearch");
            verifyThat("#barSearch", isInvisible());
        });
    }

    public void part9Exit() {
        step("Restart new session", () -> {
            validator.setUpFileDialogue(FileDialogueType.NEW_SESSION, FileFormatType.DOCUMENT, BOOK_2);
            robot.clickOn("#buttonNew");
            verifyThat("#mainWordPane", isVisible());
            verifyThat("#mainWord", hasText("the"));
        });
        step("Make change to session", () -> {
            robot.clickOn("#buttonKnown");
            verifyThat("#mainWord", hasText("a"));
        });
        step("Cancel exit", () -> {
            robot.clickOn("#menuFile");
            robot.clickOn("#menuExit");
            robot.clickOn(lookup("#unsavedChanges", "Cancel"));
            verifyThat("#mainWord", hasText("a"));
        });
        step("Exit with save", () -> {
            validator.setUpFileDialogue(FileDialogueType.SAVE_SESSION, FileFormatType.SESSION, sessionFile);
            robot.clickOn("#menuFile");
            robot.clickOn("#menuExit");
            robot.clickOn(lookup("#unsavedChanges", "Save"));
            validator.validateSavedSession(sessionFile, BOOK_2);
        });
    }

    private void step(final String step, final Runnable runnable) {
        ++stepNo;
        LOG.info("STEP {}: Begin - {}", stepNo, step);
        runnable.run();
        LOG.info("STEP {}:   End - {}", stepNo, step);
    }

    private Node lookup(final String first, final String... queries) {
        NodeQuery nodeQuery = robot.lookup(first);

        for (String query : queries) {
            nodeQuery = nodeQuery.lookup(query);
        }

        return nodeQuery.query();
    }
}
