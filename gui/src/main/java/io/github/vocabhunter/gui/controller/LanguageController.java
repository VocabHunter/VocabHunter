/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.SupportedLocale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class LanguageController {
    private final I18nManager i18nManager;

    @FXML
    private VBox boxButtons;

    @Inject
    public LanguageController(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void initialise() {
        List<Button> buttons = Arrays.stream(SupportedLocale.values())
            .map(l -> new Button(i18nManager.text(l, I18nKey.LANGUAGE_NAME)))
            .collect(toList());

        boxButtons.getChildren().addAll(buttons);
    }
}
