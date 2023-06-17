/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.SupportedLocale;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.status.StatusManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LanguageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LanguageHandler.class);

    private final MainModel mainModel;

    private final GuiFileHandler guiFileHandler;

    private final StatusManager statusManager;

    private final I18nManager i18nManager;

    private final SettingsManager settingsManager;

    private Runnable sceneSwitcher;

    @Inject
    public LanguageHandler(final MainModel mainModel, final GuiFileHandler guiFileHandler, final StatusManager statusManager, final I18nManager i18nManager, final SettingsManager settingsManager) {
        this.mainModel = mainModel;
        this.guiFileHandler = guiFileHandler;
        this.statusManager = statusManager;
        this.i18nManager = i18nManager;
        this.settingsManager = settingsManager;
    }

    public void initialise() {
        SupportedLocale locale = settingsManager.getLocale()
            .orElse(null);

        mainModel.setLocale(locale);
        if (locale == null) {
            locale = SupportedLocale.DEFAULT_LOCALE;
        }

        i18nManager.setupLocale(locale);
    }

    public void initialiseSceneSwitcher(final Runnable sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }

    public void setupControl(final MenuItem menuLanguage) {
        menuLanguage.setOnAction(e -> openChangeLanguageDialogueAction());
    }

    private void openChangeLanguageDialogueAction() {
        if (guiFileHandler.unsavedChangesCheck()) {
            mainModel.clearSessionModel();
            statusManager.clearSession();
            mainModel.setLocale(null);
            sceneSwitcher.run();
        }
    }

    public void setupLanguageSelectionControl(final SupportedLocale locale, final Button button) {
        button.setOnAction(e -> localeSelectionAction(locale));
    }

    private void localeSelectionAction(final SupportedLocale locale) {
        settingsManager.setLocale(locale);
        i18nManager.setupLocale(locale);
        mainModel.setLocale(locale);
        sceneSwitcher.run();
        LOG.info("Language set to {}", locale);
    }
}
