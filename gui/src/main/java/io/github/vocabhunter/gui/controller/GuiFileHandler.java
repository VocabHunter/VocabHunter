/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.UnsavedChangesDialogue;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
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

    public GuiFileHandler(final Stage stage, final GuiFactory factory, final SessionFileService sessionFileService, final StatusManager statusManager,
                          final MainModel model, final StatusActionService statusActionService, final SessionStateHandler sessionStateHandler) {
        this.stage = stage;
        this.factory = factory;
        this.sessionFileService = sessionFileService;
        this.statusManager = statusManager;
        this.model = model;
        this.statusActionService = statusActionService;
        this.sessionStateHandler = sessionStateHandler;
    }

    public void handleExport() {
        statusManager.beginExport();
        statusActionService.updateStatusThenRun(this::processExportInternal);
    }

    private void processExportInternal() {
        try {
            if (processFile(factory::exportSelectionChooser, this::processExport)) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }

    private boolean processFileWithCheck(final Function<Stage, FileDialogue> chooserFactory, final Function<FileDialogue, Boolean> processor) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            return processFile(chooserFactory, processor);
        } else {
            return false;
        }
    }

    public void processOpenOrNew(final Path file) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            LOG.info("Opening file '{}'", file);
            processOpen(file, sessionFileService::createOrOpenSession);
        }
    }

    private boolean processFile(final Function<Stage, FileDialogue> chooserFactory, final Function<FileDialogue, Boolean> processor) {
        FileDialogue chooser = chooserFactory.apply(stage);

        chooser.showChooser();
        if (chooser.isFileSelected()) {
            return processor.apply(chooser);
        } else {
            return false;
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
                    return processSave();
                case DISCARD:
                    return true;
                default:
                    return false;
            }
        }
    }

    public void handleOpenSession() {
        statusManager.beginOpenSession();
        statusActionService.updateStatusThenRun(this::processOpenSessionInternal);
    }

    private void processOpenSessionInternal() {
        try {
            boolean isSuccessfulAction = processFileWithCheck(factory::openSessionChooser, this::processOpenSession);

            if (isSuccessfulAction) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }

    private boolean processOpenSession(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();

        LOG.info("Opening session file '{}'", file);
        return processOpen(file, sessionFileService::read);
    }

    public void handleNewSession() {
        statusManager.beginNewSession();
        statusActionService.updateStatusThenRun(this::processNewSessionInternal);
    }

    private void processNewSessionInternal() {
        try {
            if (processFileWithCheck(factory::newSessionChooser, this::processNewSession)) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }

    private boolean processNewSession(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();

        LOG.info("New session from '{}'", file);
        return processOpen(file, sessionFileService::createNewSession);
    }

    private boolean processOpen(final Path file, final Function<Path, EnrichedSessionState> opener) {
        try {
            EnrichedSessionState enrichedState = opener.apply(file);
            SessionState state = enrichedState.getState();
            SessionModel sessionModel = sessionStateHandler.addSession(state);

            model.replaceSessionModel(state, sessionModel, enrichedState.getFile().orElse(null));
            statusManager.replaceSession(sessionModel.getPosition(), sessionModel.getProgress());

            return true;
        } catch (final RuntimeException e) {
            FileErrorTool.open(file, e);

            return false;
        }
    }

    public void handleSave() {
        statusManager.beginSaveSession();
        statusActionService.updateStatusThenRun(this::processSaveInternal);
    }

    private void processSaveInternal() {
        try {
            if (processSave()) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }

    private boolean processSave() {
        if (model.hasSessionFile()) {
            saveFile();

            return true;
        } else {
            return saveAsInternal();
        }
    }

    public void handleSaveAs() {
        statusManager.beginSaveSession();
        statusActionService.updateStatusThenRun(this::processSaveAs);
    }

    private void processSaveAs() {
        try {
            if (saveAsInternal()) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }

    private boolean saveAsInternal() {
        FileDialogue chooser = factory.saveSessionChooser(stage);

        chooser.showChooser();
        if (chooser.isFileSelected()) {
            Path file = chooser.getSelectedFile();
            file = FileNameTool.ensureSessionFileHasSuffix(file);

            model.setSessionFile(file);

            return saveFile();
        } else {
            return false;
        }
    }

    private boolean saveFile() {
        statusManager.performAction();

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

    private boolean processExport(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();
        file = FileNameTool.ensureExportFileHasSuffix(file);

        try {
            LOG.info("Exporting to file '{}'", file);
            sessionFileService.exportSelection(sessionStateHandler.getSessionState(), file);

            return true;
        } catch (final RuntimeException e) {
            FileErrorTool.export(file, e);

            return false;
        }
    }

}
