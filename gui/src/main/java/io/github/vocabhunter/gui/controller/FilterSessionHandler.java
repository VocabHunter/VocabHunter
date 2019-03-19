/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.fxml.FXMLLoader;

import javax.inject.Inject;
import javax.inject.Provider;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_SESSION_WINDOW_TITLE;

public class FilterSessionHandler extends BaseFilterHandler<FilterSessionController> {
    @Inject
    public FilterSessionHandler(final Provider<FXMLLoader> loaderProvider, final I18nManager i18nManager) {
        super(loaderProvider, i18nManager, ViewFxml.FILTER_SESSION, FILTER_SESSION_WINDOW_TITLE);
    }
}
