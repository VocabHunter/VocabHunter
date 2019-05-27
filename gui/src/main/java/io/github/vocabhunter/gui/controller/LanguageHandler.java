/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.SupportedLocale;
import io.github.vocabhunter.gui.model.MainModel;
import javafx.scene.control.RadioMenuItem;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LanguageHandler {
    private final MainModel mainModel;

    private final GuiFileHandler guiFileHandler;

    private final Map<SupportedLocale, RadioMenuItem> itemMap = new EnumMap<>(SupportedLocale.class);

    private Consumer<SupportedLocale> localeChangeConsumer;

    @Inject
    public LanguageHandler(final MainModel mainModel, final GuiFileHandler guiFileHandler) {
        this.mainModel = mainModel;
        this.guiFileHandler = guiFileHandler;
    }

    public void initialise(final Consumer<SupportedLocale> localeChangeConsumer) {
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
            mainModel.setLocale(locale);
            localeChangeConsumer.accept(locale);
        }
        updateSelection();
    }

    private void updateSelection() {
        SupportedLocale locale = mainModel.getLocale();

        itemMap.forEach((l, i) -> i.setSelected(l == locale));
    }
}
