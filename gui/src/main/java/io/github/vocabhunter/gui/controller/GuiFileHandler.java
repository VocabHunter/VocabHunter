/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.common.GuiTaskHandler;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.services.SessionFileService;
import io.github.vocabhunter.gui.status.GuiTask;
import io.github.vocabhunter.gui.status.StatusManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Singleton
public class GuiFileHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GuiFileHandler.class);

    private Stage stage;

    @Inject
    private FileDialogueFactory fileDialogueFactory;

    @Inject
    private SessionFileService sessionFileService;

    @Inject
    private StatusManager statusManager;

    @Inject
    private MainModel model;

    @Inject
    private SessionStateHandler sessionStateHandler;

    @Inject
    private GuiTaskHandler guiTaskHandler;

    @Inject
    private DialogueTool dialogueTool;

    public void initialise(final Stage stage) {
        this.stage = stage;
    }

    public void handleExportWithNotes() {
        handleExport(true);
    }

    public void handleExportWithoutNotes() {
        handleExport(false);
    }

    private void handleExport(final boolean isNoteIncluded) {
        if (statusManager.beginExport()) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(() -> processExport(isNoteIncluded));
        }
    }

    private void processExport(final boolean isNoteIncluded) {
        Path file = chooseFile(FileDialogueType.EXPORT_SELECTION);

        if (file == null) {
            statusManager.completeAction();
        } else {
            statusManager.performAction(file);

            Path fileWithSuffix = FileNameTool.ensureExportFileHasSuffix(file);
            SessionState sessionState = sessionStateHandler.getSessionState();
            GuiTask<Boolean> task = new GuiTask<>(
                guiTaskHandler,
                statusManager,
                () -> processExport(fileWithSuffix, sessionState, isNoteIncluded),
                e -> dialogueTool.errorOnExport(fileWithSuffix, e)
            );

            guiTaskHandler.executeInBackground(task);
        }
    }

    private boolean processExport(final Path file, final SessionState sessionState, final boolean isNoteIncluded) {
        LOG.info("Exporting to file '{}' (Notes included: {})", file, isNoteIncluded);
        sessionFileService.exportSelection(sessionState, file, isNoteIncluded);

        return true;
    }

    public void processOpenOrNew(final Path file) {
        if (statusManager.beginOpenSession()) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(() -> processOpenOrNewInternal(file));
        }
    }

    private void processOpenOrNewInternal(final Path file) {
        if (unsavedChangesCheck()) {
            LOG.info("Opening file '{}'", file);

            GuiTask<EnrichedSessionState> task = new GuiTask<>(
                guiTaskHandler,
                statusManager,
                () -> sessionFileService.createOrOpenSession(file),
                this::finishOpen,
                e -> dialogueTool.errorOnOpen(file, e));

            guiTaskHandler.executeInBackground(task);
        } else {
            statusManager.completeAction();
        }
    }

    public void handleOpenSession() {
        if (statusManager.beginOpenSession()) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(this::processOpenSession);
        }
    }

    private void processOpenSession() {
        Path file = checkUnsavedChangesAndChooseFile(FileDialogueType.OPEN_SESSION);

        if (file == null) {
            statusManager.completeAction();
        } else {
            statusManager.performAction(file);
            LOG.info("Opening session file '{}'", file);

            GuiTask<EnrichedSessionState> task = new GuiTask<>(
                guiTaskHandler,
                statusManager,
                () -> sessionFileService.read(file),
                this::finishOpen,
                e -> dialogueTool.errorOnOpen(file, e));

            guiTaskHandler.executeInBackground(task);
        }
    }

    public void handleNewSession() {
        if (statusManager.beginNewSession()) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(this::processNewSession);
        }
    }

    private void processNewSession() {
        Path file = checkUnsavedChangesAndChooseFile(FileDialogueType.NEW_SESSION);

        if (file == null) {
            statusManager.completeAction();
        } else {
            statusManager.performAction(file);
            LOG.info("New session from '{}'", file);

            GuiTask<EnrichedSessionState> task = new GuiTask<>(
                guiTaskHandler,
                statusManager,
                () -> sessionFileService.createNewSession(file),
                this::finishOpen,
                e -> dialogueTool.errorOnOpen(file, e));

            guiTaskHandler.executeInBackground(task);
        }
    }

    private void finishOpen(final EnrichedSessionState enrichedState) {
        SessionState state = enrichedState.getState();
        SessionModel sessionModel = sessionStateHandler.addSession(state);

        model.replaceSessionModel(state, sessionModel, enrichedState.getFile().orElse(null));
        statusManager.replaceSession(sessionModel.getPosition(), sessionModel.getProgress());
    }

    public void handleSave() {
        if (model.hasSessionFile()) {
            if (statusManager.beginSaveSession()) {
                guiTaskHandler.pauseThenExecuteOnGuiThread(this::processSave);
            }
        } else {
            handleSaveAs();
        }
    }

    public void handleSaveAs() {
        if (statusManager.beginSaveSession()) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(this::processSaveAs);
        }
    }

    private void processSaveAs() {
        Path file = chooseFile(FileDialogueType.SAVE_SESSION);

        if (file == null) {
            statusManager.completeAction();
        } else {
            file = FileNameTool.ensureSessionFileHasSuffix(file);
            model.setSessionFile(file);
            processSave();
        }
    }

    private void processSave() {
        Path file = model.getSessionFile();

        statusManager.performAction(file);
        LOG.info("Saving file '{}'", file);

        SessionState sessionState = sessionStateHandler.getSessionState();
        GuiTask<Boolean> task = new GuiTask<>(
            guiTaskHandler,
            statusManager,
            () -> saveFile(file, sessionState),
            b -> model.setChangesSaved(true),
            e -> dialogueTool.errorOnSave(file, e)
        );

        guiTaskHandler.executeInBackground(task);
    }

    private boolean saveFile(final Path file, final SessionState sessionState) {
        sessionFileService.write(file, sessionState);

        return true;
    }

    private Path checkUnsavedChangesAndChooseFile(final FileDialogueType type) {
        if (unsavedChangesCheck()) {
            return chooseFile(type);
        } else {
            return null;
        }
    }

    private Path chooseFile(final FileDialogueType type) {
        FileDialogue dialogue = fileDialogueFactory.create(type, stage);

        dialogue.showChooser();
        if (dialogue.isFileSelected()) {
            return dialogue.getSelectedFile();
        } else {
            return null;
        }
    }

    public boolean unsavedChangesCheck() {
        if (model.isChangesSaved()) {
            return true;
        } else {
            UnsavedChangesDialogue dialogue = dialogueTool.unsavedChangesDialogue(model.getSessionFile());

            dialogue.showDialogue();

            return switch (dialogue.getUserResponse()) {
                case SAVE -> saveChanges();
                case DISCARD -> true;
                case CANCEL -> false;
            };
        }
    }

    private boolean saveChanges() {
        if (model.hasSessionFile()) {
            saveChangesInternal();

            return true;
        } else {
            return saveChangesAs();
        }
    }

    private boolean saveChangesAs() {
        Path file = chooseFile(FileDialogueType.SAVE_SESSION);

        if (file == null) {
            return false;
        } else {
            file = FileNameTool.ensureSessionFileHasSuffix(file);

            model.setSessionFile(file);

            return saveChangesInternal();
        }
    }

    private boolean saveChangesInternal() {
        Path file = model.getSessionFile();

        try {
            LOG.info("Saving file '{}'", file);
            sessionFileService.write(file, sessionStateHandler.getSessionState());
            model.setChangesSaved(true);

            return true;
        } catch (final RuntimeException e) {
            dialogueTool.errorOnSave(file, e);

            return false;
        }
    }
}
