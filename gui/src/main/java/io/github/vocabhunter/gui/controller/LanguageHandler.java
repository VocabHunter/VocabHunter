/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.SupportedLocale;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.status.StatusManager;
import javafx.scene.control.RadioMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LanguageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LanguageHandler.class);

    private final MainModel mainModel;

    private final GuiFileHandler guiFileHandler;

    private final StatusManager statusManager;

    private final I18nManager i18nManager;

    private final Map<SupportedLocale, RadioMenuItem> itemMap = new EnumMap<>(SupportedLocale.class);

    private Runnable localeChangeConsumer;

    @Inject
    public LanguageHandler(final MainModel mainModel, final GuiFileHandler guiFileHandler, final StatusManager statusManager, final I18nManager i18nManager) {
        this.mainModel = mainModel;
        this.guiFileHandler = guiFileHandler;
        this.statusManager = statusManager;
        this.i18nManager = i18nManager;
    }

    public void initialise(final Runnable localeChangeConsumer) {
        this.localeChangeConsumer = localeChangeConsumer;
    }

    public void setupControls(final RadioMenuItem menuEnglish, final RadioMenuItem menuSpanish) {
        setupLocale(menuEnglish, SupportedLocale.ENGLISH);
        setupLocale(menuSpanish, SupportedLocale.SPANISH);
        updateSelection();
    }

    private void setupLocale(final RadioMenuItem menuItem, final SupportedLocale locale) {
        itemMap.put(locale, menuItem);
        menuItem.setOnAction(e -> localeSelectionAction(locale));
    }

    private void localeSelectionAction(final SupportedLocale locale) {
        if (mainModel.getLocale() != locale && guiFileHandler.unsavedChangesCheck()) {
            i18nManager.setupLocale(locale);
            mainModel.setLocale(locale);
            mainModel.clearSessionModel();
            statusManager.clearSession();
            localeChangeConsumer.run();
            LOG.info("Language changed to {}", locale);
        }
        updateSelection();
    }

    private void updateSelection() {
        SupportedLocale locale = mainModel.getLocale();

        itemMap.forEach((l, i) -> i.setSelected(l == locale));
    }
}
