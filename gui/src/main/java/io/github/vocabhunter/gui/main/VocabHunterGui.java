/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.controller.*;
import io.github.vocabhunter.gui.model.FilterSettingsTool;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.services.ExternalEventBroker;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabHunterGui {
    private static final Logger LOG = LoggerFactory.getLogger(VocabHunterGui.class);

    private static final int NANOS_PER_MILLI = 1_000_000;

    private final LanguageHandler languageHandler;

    private final GuiFileHandler guiFileHandler;

    private final TitleHandler titleHandler;

    private final MainModel model;

    private final FilterHandler filterHandler;

    private final ExitRequestHandler exitRequestHandler;

    private final FilterSettingsTool filterSettingsTool;

    private final MainStageHandler mainStageHandler;

    private final ExternalEventBroker externalEventBroker;

    @Inject
    public VocabHunterGui(final LanguageHandler languageHandler, final GuiFileHandler guiFileHandler, final TitleHandler titleHandler, final MainModel model,
        final FilterHandler filterHandler, final ExitRequestHandler exitRequestHandler, final FilterSettingsTool filterSettingsTool,
        final MainStageHandler mainStageHandler, final ExternalEventBroker externalEventBroker) {
        this.languageHandler = languageHandler;
        this.guiFileHandler = guiFileHandler;
        this.titleHandler = titleHandler;
        this.model = model;
        this.filterHandler = filterHandler;
        this.exitRequestHandler = exitRequestHandler;
        this.filterSettingsTool = filterSettingsTool;
        this.mainStageHandler = mainStageHandler;
        this.externalEventBroker = externalEventBroker;
    }

    public void start(final Stage stage, final long startupTimestampNanos) {
        languageHandler.initialise();
        mainStageHandler.initialise(stage);
        mainStageHandler.applyNewScene();
        initialise(stage);

        stage.show();

        Platform.runLater(() -> logStartup(startupTimestampNanos));

        // We delay starting the async filtering to allow the GUI to start quickly
        Platform.runLater(filterSettingsTool::beginAsyncFiltering);

        externalEventBroker.markGuiOpen(this::processOpenOrNew);
    }

    private void initialise(final Stage stage) {
        guiFileHandler.initialise(stage);
        exitRequestHandler.initialise(stage);
        titleHandler.initialise();
        filterHandler.initialise();

        stage.titleProperty().bind(model.titleProperty());
    }

    private void logStartup(final long startupTimestampNanos) {
        long currentTimestampNanos = System.nanoTime();
        long startupMillis = (currentTimestampNanos - startupTimestampNanos) / NANOS_PER_MILLI;
        String startupTimeText = String.format("%,d", startupMillis);

        LOG.info("User interface started ({} ms)", startupTimeText);
    }

    private void processOpenOrNew(final Path file) {
        Platform.runLater(() -> guiFileHandler.processOpenOrNew(file));
    }
}
