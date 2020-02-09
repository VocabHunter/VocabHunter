/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.SupportedLocale;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class LanguageController {
    private static final String STYLE_BUTTON = "languageButton";

    private static final Duration FADE_DURATION = Duration.seconds(1.5);

    private final I18nManager i18nManager;

    private final LanguageHandler languageHandler;

    @FXML
    private Label labelTitle;

    @FXML
    private VBox boxLanguageButtons;

    private final SupportedLocale[] allLocales = SupportedLocale.values();

    private int currentTitleIndex = 0;

    private final FadeTransition titleTransition = new FadeTransition(FADE_DURATION);

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
        titleTransition.setFromValue(1);
        titleTransition.setToValue(1);
        titleTransition.setNode(labelTitle);
        titleTransition.setOnFinished(e -> Platform.runLater(this::animationNextStep));
        applyTitleText();
        titleTransition.play();
    }

    private void animationNextStep() {
        applyTitleText();
        currentTitleIndex = (currentTitleIndex + 1) % allLocales.length;
        titleTransition.setToValue(0);
        titleTransition.play();
    }

    private void applyTitleText() {
        labelTitle.setText(i18nManager.text(allLocales[currentTitleIndex], I18nKey.LANGUAGE_TITLE));
    }

    private void initialiseButtons() {
        List<Button> buttons = Arrays.stream(SupportedLocale.values())
            .map(this::button)
            .collect(toList());

        boxLanguageButtons.getChildren().addAll(buttons);
    }

    private Button button(final SupportedLocale l) {
        Button button = new Button(i18nManager.text(l, I18nKey.LANGUAGE_NAME));

        button.setId(l.name());
        button.getStyleClass().add(STYLE_BUTTON);
        languageHandler.setupLanguageSelectionControl(l, button);

        return button;
    }

    public void closeView() {
        Platform.runLater(this::stopTitleAnimation);
    }

    private void stopTitleAnimation() {
        titleTransition.setNode(null);
        titleTransition.setOnFinished(null);
        titleTransition.stop();
    }
}
