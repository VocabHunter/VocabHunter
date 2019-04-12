/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.SupportedLocale;
import io.github.vocabhunter.gui.model.MainModel;
import javafx.scene.control.RadioMenuItem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LanguageHandler {
    private final MainModel mainModel;

    @Inject
    public LanguageHandler(final MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void initialise(final RadioMenuItem menuEnglish, final RadioMenuItem menuSpanish) {
        menuEnglish.setSelected(true);
        menuEnglish.setOnAction(e -> mainModel.setLocale(SupportedLocale.ENGLISH));
        menuSpanish.setOnAction(e -> mainModel.setLocale(SupportedLocale.SPANISH));
    }
}
