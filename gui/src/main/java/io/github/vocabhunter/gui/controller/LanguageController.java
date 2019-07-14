/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.SupportedLocale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class LanguageController {
    private static final String STYLE_BUTTON = "languageButton";

    private final I18nManager i18nManager;

    private final LanguageHandler languageHandler;

    @FXML
    private Label labelTitle;

    @FXML
    private VBox boxLanguageButtons;

    @Inject
    public LanguageController(final I18nManager i18nManager, final LanguageHandler languageHandler) {
        this.i18nManager = i18nManager;
        this.languageHandler = languageHandler;
    }

    public void initialise() {
        initialiseTitle();
        initialiseButtons();
    }

    private void initialiseTitle() {
        String title = Arrays.stream(SupportedLocale.values())
            .map(l -> i18nManager.text(l, I18nKey.LANGUAGE_TITLE))
            .collect(Collectors.joining(" | "));

        labelTitle.setText(title);
    }

    private void initialiseButtons() {
        List<Button> buttons = Arrays.stream(SupportedLocale.values())
            .map(this::button)
            .collect(toList());

        boxLanguageButtons.getChildren().addAll(buttons);
    }

    private Button button(final SupportedLocale l) {
        Button button = new Button(i18nManager.text(l, I18nKey.LANGUAGE_NAME));

        button.getStyleClass().add(STYLE_BUTTON);
        languageHandler.setupLanguageSelectionControl(l, button);

        return button;
    }
}
