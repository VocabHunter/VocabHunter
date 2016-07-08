/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.CoreConstants;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.FileListManagerImpl;
import io.github.vocabhunter.gui.common.EnvironmentManager;
import io.github.vocabhunter.gui.common.WebPageTool;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;
import io.github.vocabhunter.gui.status.StatusActionManagerForTesting;
import io.github.vocabhunter.gui.status.StatusManager;
import io.github.vocabhunter.test.utils.TestFileManager;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.MutablePicoContainer;
import org.testfx.api.FxRobot;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.vocabhunter.gui.main.GuiTestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;

@RunWith(MockitoJUnitRunner.class)
public class GuiTest extends FxRobot implements GuiTestValidator {
    private TestFileManager manager;

    @Mock
    EnvironmentManager environmentManager;

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

    @Mock
    private WebPageTool webPageTool;

    @Captor
    private ArgumentCaptor<String> webPageCaptor;

    private Path exportFile;

    private Path sessionFile;

    @BeforeClass
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        registerPrimaryStage();
    }

    @Before
    public void setUp() throws Exception {
        when(environmentManager.getScreenSize()).thenReturn(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        when(environmentManager.useSystemMenuBar()).thenReturn(false);

        manager = new TestFileManager(getClass());
        exportFile = manager.addFile("export.txt");
        sessionFile = manager.addFile("session.wordy");

        Path document1 = getResource(BOOK1);
        Path document2 = getResource(BOOK2);

        setUpFileDialogue(FileDialogueType.NEW_SESSION, newSessionDialogue, document1, document2);
        setUpFileDialogue(FileDialogueType.SAVE_SESSION, saveSessionDialogue, sessionFile);
        setUpFileDialogue(FileDialogueType.OPEN_SESSION, openSessionDialogue, sessionFile);
        setUpFileDialogue(FileDialogueType.EXPORT_SELECTION, exportDialogue, exportFile);

        Path settingsFile = manager.addFile(SettingsManagerImpl.SETTINGS_JSON);
        SettingsManager settingsManager = new SettingsManagerImpl(settingsFile);
        Path fileListManagerFile = manager.addFile(FileListManagerImpl.SETTINGS_JSON);
        FileListManager fileListManager = new FileListManagerImpl(fileListManagerFile);

        MutablePicoContainer pico = GuiContainerBuilder.createBaseContainer();
        pico.addComponent(settingsManager);
        pico.addComponent(fileListManager);
        pico.addComponent(fileDialogueFactory);
        pico.addComponent(environmentManager);
        pico.addComponent(webPageTool);
        pico.addComponent(new StatusActionManagerForTesting(pico.getComponent(StatusManager.class)));
        VocabHunterGuiExecutable.setPico(pico);

        setupApplication(VocabHunterGuiExecutable.class);
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
    public void testWalkThrough() {
        GuiTestSteps steps = new GuiTestSteps(this, this);

        steps.part1BasicWalkThrough();
        steps.part2Progress();
        steps.part3StartNewSessionAndFilter();
        steps.part4ReopenFirstSession();
        steps.part5AboutDialogue();
        steps.part6WebLinks();
    }

    @Override
    public void validateExportFile() {
        String text = readFile(exportFile);
        assertThat("Export file content", text, containsString("the"));
    }

    @Override
    public void validateWebPage(final String page) {
        verify(webPageTool, atLeastOnce()).showWebPage(webPageCaptor.capture());
        assertEquals("Website", page, webPageCaptor.getValue());
    }

    private String readFile(final Path file) {
        try {
            return new String(Files.readAllBytes(file), CoreConstants.CHARSET);
        } catch (IOException e) {
            throw new VocabHunterException(String.format("Unable to read file %s", file), e);
        }
    }

    @Override
    public void validateSavedSession(final String name) {
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
}
