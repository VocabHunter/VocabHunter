/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.controller.*;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.FilterSettingsTool;
import io.github.vocabhunter.gui.model.MainModel;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.i18n.SupportedLocale.DEFAULT_LOCALE;

@Singleton
public class VocabHunterGui {
    private static final Logger LOG = LoggerFactory.getLogger(VocabHunterGui.class);

    private static final int NANOS_PER_MILLI = 1_000_000;

    private final I18nManager i18nManager;

    private final GuiFileHandler guiFileHandler;

    private final TitleHandler titleHandler;

    private final MainModel model;

    private final FilterHandler filterHandler;

    private final ExitRequestHandler exitRequestHandler;

    private final FilterSettingsTool filterSettingsTool;

    private final MainStageHandler mainStageHandler;

    @Inject
    public VocabHunterGui(final I18nManager i18nManager, final GuiFileHandler guiFileHandler, final TitleHandler titleHandler, final MainModel model,
        final FilterHandler filterHandler, final ExitRequestHandler exitRequestHandler, final FilterSettingsTool filterSettingsTool, final MainStageHandler mainStageHandler) {
        this.i18nManager = i18nManager;
        this.guiFileHandler = guiFileHandler;
        this.titleHandler = titleHandler;
        this.model = model;
        this.filterHandler = filterHandler;
        this.exitRequestHandler = exitRequestHandler;
        this.filterSettingsTool = filterSettingsTool;
        this.mainStageHandler = mainStageHandler;
    }

    public void start(final Stage stage, final long startupTimestampNanos) {
        i18nManager.setupLocale(DEFAULT_LOCALE);
        mainStageHandler.initialise(stage);
        mainStageHandler.applyNewScene();
        initialise(stage);

        stage.show();

        Platform.runLater(() -> logStartup(startupTimestampNanos));

        // We delay starting the async filtering to allow the GUI to start quickly
        Platform.runLater(filterSettingsTool::beginAsyncFiltering);
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
}
