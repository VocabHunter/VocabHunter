/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.UnsavedChangesDialogue;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.status.GuiTask;
import io.github.vocabhunter.gui.status.StatusActionService;
import io.github.vocabhunter.gui.status.StatusManager;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Function;

public class GuiFileHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GuiFileHandler.class);

    private final Stage stage;

    private final GuiFactory factory;

    private final SessionFileService sessionFileService;

    private final StatusManager statusManager;

    private final MainModel model;

    private final StatusActionService statusActionService;

    private final SessionStateHandler sessionStateHandler;

    private final GuiTaskHandler guiTaskHandler;

    public GuiFileHandler(final Stage stage, final GuiFactory factory, final SessionFileService sessionFileService, final StatusManager statusManager,
                          final MainModel model, final StatusActionService statusActionService, final SessionStateHandler sessionStateHandler, final GuiTaskHandler guiTaskHandler) {
        this.stage = stage;
        this.factory = factory;
        this.sessionFileService = sessionFileService;
        this.statusManager = statusManager;
        this.model = model;
        this.statusActionService = statusActionService;
        this.sessionStateHandler = sessionStateHandler;
        this.guiTaskHandler = guiTaskHandler;
    }

    public void handleExport() {
        if (statusManager.beginExport()) {
            statusActionService.updateStatusThenRun(this::processExport);
        }
    }

    private void processExport() {
        Path file = chooseFile(factory::exportSelectionChooser);

        if (file == null) {
            statusManager.completeAction();
        } else {
            statusManager.performAction(file);

            Path fileWithSuffix = FileNameTool.ensureExportFileHasSuffix(file);
            SessionState sessionState = sessionStateHandler.getSessionState();
            GuiTask<Boolean> task = new GuiTask<>(
                guiTaskHandler,
                statusManager,
                () -> processExport(fileWithSuffix, sessionState),
                e -> FileErrorTool.export(fileWithSuffix, e)
            );

            guiTaskHandler.executeInBackground(task);
        }
    }

    private boolean processExport(final Path file, final SessionState sessionState) {
        LOG.info("Exporting to file '{}'", file);
        sessionFileService.exportSelection(sessionState, file);

        return true;
    }

    public void processOpenOrNew(final Path file) {
        if (statusManager.beginOpenSession()) {
            statusActionService.updateStatusThenRun(() -> processOpenOrNewInternal(file));
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
                e -> FileErrorTool.open(file, e));

            guiTaskHandler.executeInBackground(task);
        } else {
            statusManager.completeAction();
        }
    }

    public void handleOpenSession() {
        if (statusManager.beginOpenSession()) {
            statusActionService.updateStatusThenRun(this::processOpenSession);
        }
    }

    private void processOpenSession() {
        Path file = checkUnsavedChangesAndChooseFile(factory::openSessionChooser);

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
                e -> FileErrorTool.open(file, e));

            guiTaskHandler.executeInBackground(task);
        }
    }

    public void handleNewSession() {
        if (statusManager.beginNewSession()) {
            statusActionService.updateStatusThenRun(this::processNewSession);
        }
    }

    private void processNewSession() {
        Path file = checkUnsavedChangesAndChooseFile(factory::newSessionChooser);

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
                e -> FileErrorTool.open(file, e));

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
                statusActionService.updateStatusThenRun(this::processSave);
            }
        } else {
            handleSaveAs();
        }
    }

    public void handleSaveAs() {
        if (statusManager.beginSaveSession()) {
            statusActionService.updateStatusThenRun(this::processSaveAs);
        }
    }

    private void processSaveAs() {
        Path file = chooseFile(factory::saveSessionChooser);

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
            e -> FileErrorTool.save(file, e)
        );

        guiTaskHandler.executeInBackground(task);
    }

    private boolean saveFile(final Path file, final SessionState sessionState) {
        sessionFileService.write(file, sessionState);

        return true;
    }

    private Path checkUnsavedChangesAndChooseFile(final Function<Stage, FileDialogue> f) {
        if (unsavedChangesCheck()) {
            return chooseFile(f);
        } else {
            return null;
        }
    }

    private Path chooseFile(final Function<Stage, FileDialogue> f) {
        FileDialogue dialogue = f.apply(stage);

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
            UnsavedChangesDialogue dialogue = factory.unsavedChangesDialogue(model);

            dialogue.showDialogue();

            switch (dialogue.getUserResponse()) {
                case SAVE:
                    return saveChanges();
                case DISCARD:
                    return true;
                default:
                    return false;
            }
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
        Path file = chooseFile(factory::saveSessionChooser);

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
            FileErrorTool.save(file, e);

            return false;
        }
    }
}
