/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.factory.FileDialogueFactory;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;
import io.github.vocabhunter.test.utils.TestFileManager;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;
import static org.testfx.matcher.base.NodeMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GuiTest extends FxRobot {
    private static final String BOOK1 = "great-expectations.txt";

    private static final String BOOK2 = "oliver-twist.txt";

    private static final Logger LOG = LoggerFactory.getLogger(GuiTest.class);

    private TestFileManager manager;

    @Mock
    private FileDialogueFactory fileDialogueFactory;

    @Mock
    private FileDialogue newSessionDialogue;

    @Mock
    private FileDialogue saveSessionDialogue;

    @Mock
    private FileDialogue openSessionDialogue;

    @Mock
    private FileDialogue exportDialogue;

    private Path exportFile;

    private Path sessionFile;

    private String step;

    private int stepNo;

    @BeforeClass
    public static void setupSpec() throws Exception {
        registerPrimaryStage();
    }

    @Before
    public void setUp() throws Exception {
        manager = new TestFileManager(getClass());
        exportFile = manager.addFile("export.txt");
        sessionFile = manager.addFile("session.wordy");

        Path document1 = getResource(BOOK1);
        Path document2 = getResource(BOOK2);

        setUpFileDialogue(FileDialogueType.NEW_SESSION, newSessionDialogue, document1, document2);
        setUpFileDialogue(FileDialogueType.SAVE_SESSION, saveSessionDialogue, sessionFile);
        setUpFileDialogue(FileDialogueType.OPEN_SESSION, openSessionDialogue, sessionFile);
        setUpFileDialogue(FileDialogueType.EXPORT_SELECTION, exportDialogue, exportFile);

        Path settingsFile = manager.addFile("settings.json");
        SettingsManager settingsManager = new SettingsManagerImpl(settingsFile);

        settingsManager.setFilterMinimumLetters(1);
        settingsManager.setFilterMinimumOccurrences(1);
        TestGuiApplication.setSettingsManager(settingsManager);
        TestGuiApplication.setFileDialogueFactory(fileDialogueFactory);
        setupApplication(TestGuiApplication.class);
    }

    private void setUpFileDialogue(final FileDialogueType type, final FileDialogue dialogue, final Path file1, final Path... files) {
        when(fileDialogueFactory.create(eq(type), any(Stage.class))).thenReturn(dialogue);
        when(dialogue.isFileSelected()).thenReturn(true);
        when(dialogue.getSelectedFile()).thenReturn(file1, files);
    }

    @After
    public void tearDown() throws Exception {
        manager.cleanup();
    }

    @Test
    public void testWalkThrough() throws Exception {
        startStep("Open application");
        verifyThat("#mainBorderPane", isVisible());
        endStep();

        startStep("Start new session");
        clickOn("#buttonNew");
        verifyThat("#mainWordPane", isVisible());
        verifyThat("#mainWord", hasText("and"));
        endStep();

        startStep("Mark word as known");
        clickOn("#buttonKnown");
        verifyThat("#mainWord", hasText("the"));
        endStep();

        startStep("Mark word as unknown");
        clickOn("#buttonUnknown");
        verifyThat("#mainWord", hasText("to"));
        endStep();

        startStep("Show selection");
        clickOn("#buttonEditOff");
        verifyThat("#buttonKnown", isInvisible());
        endStep();

        startStep("Return to edit mode");
        clickOn("#buttonEditOn");
        verifyThat("#buttonKnown", isVisible());
        endStep();

        startStep("Export the selection");
        clickOn("#buttonExport");
        String text = FileUtils.readFileToString(exportFile.toFile());
        assertThat("Export file content", text, containsString("the"));
        endStep();

        startStep("Save the session");
        clickOn("#buttonSave");
        validateSavedSession(BOOK1);
        endStep();

        startStep("Open a new session for a different book");
        clickOn("#buttonNew");
        verifyThat("#mainWord", hasText("the"));
        endStep();

        startStep("Re-open the old session");
        clickOn("#buttonOpen");
        verifyThat("#mainWord", hasText("to"));
        endStep();
    }

    private void validateSavedSession(final String name) {
        EnrichedSessionState state = SessionSerialiser.read(sessionFile);

        assertEquals("Session state name", name, state.getState().getName());
    }

    private Path getResource(final String file) throws Exception {
        URL url = getClass().getResource("/" + file);

        if (url == null) {
            throw new VocabHunterException(String.format("Unable to load %s", file));
        } else {
            return Paths.get(url.toURI());
        }
    }

    private void startStep(final String step) {
        ++stepNo;
        this.step = step;

        LOG.info("STEP {}: Begin - {}", stepNo, step);
    }

    private void endStep() {
        LOG.info("STEP {}:   End - {}", stepNo, step);
    }
}
