/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.MainModel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.beans.value.ChangeListener;

@Singleton
public class TitleHandler {
    private static final int TITLE_BUFFER_SIZE = 100;

    private final MainModel model;

    private final I18nManager i18nManager;

    @Inject
    public TitleHandler(final MainModel model, final I18nManager i18nManager) {
        this.model = model;
        this.i18nManager = i18nManager;
    }

    public void initialise() {
        ChangeListener<Object> listener = (o, old, v) -> updateTitle();

        model.sessionFileProperty().addListener(listener);
        model.documentNameProperty().addListener(listener);
        model.changesSavedProperty().addListener(listener);
        model.localeProperty().addListener(listener);

        updateTitle();
    }

    private void updateTitle() {
        StringBuilder title = new StringBuilder(TITLE_BUFFER_SIZE);

        if (model.isSessionOpen()) {
            if (model.hasSessionFile()) {
                title.append(model.getSessionFile().getFileName());
            } else {
                title.append(i18nManager.text(I18nKey.MAIN_WINDOW_UNTITLED));
            }

            String documentName = model.getDocumentName();

            if (documentName != null) {
                title.append(" (").append(documentName).append(')');
            }

            if (!model.changesSavedProperty().get()) {
                title.append(" - ").append(i18nManager.text(I18nKey.MAIN_WINDOW_UNSAVED));
            }

            title.append(" - ");
        }

        title.append("VocabHunter");
        model.setTitle(title.toString());
    }
}
