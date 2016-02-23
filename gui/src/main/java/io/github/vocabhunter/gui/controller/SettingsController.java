/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SettingsController {
    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

    public TextField fieldMinimumLetters;

    public TextField fieldMinimumOccurrences;

    public Button buttonOk;

    public Button buttonCancel;

    private SettingsManager settingsManager;

    private Stage stage;

    public void initialise(final SettingsManager settingsManager, final Stage stage) {
        this.settingsManager = settingsManager;
        this.stage = stage;

        buttonOk.setOnAction(e -> exit(true));
        buttonCancel.setOnAction(e -> exit(false));
        initialiseField(fieldMinimumLetters, settingsManager::getFilterMinimumLetters);
        initialiseField(fieldMinimumOccurrences, settingsManager::getFilterMinimumOccurrences);
    }

    private void exit(final boolean isSaveRequested) {
        if (isSaveRequested) {
            saveField(fieldMinimumLetters, SettingsManager::setFilterMinimumLetters);
            saveField(fieldMinimumOccurrences, SettingsManager::setFilterMinimumOccurrences);
        }
        stage.close();
    }

    private void saveField(final TextField field, final BiConsumer<SettingsManager, Integer> setter) {
        String text = field.getText();

        try {
            if (!text.isEmpty()) {
                int value = Integer.parseInt(text);

                setter.accept(settingsManager, value);
            }
        } catch (NumberFormatException e) {
            throw new VocabHunterException(String.format("Unable to parse field value '%s'", text), e);
        }
    }

    private void initialiseField(final TextField field, final Supplier<Integer> settingGetter) {
        StringProperty textProperty = field.textProperty();
        ReadOnlyBooleanProperty focusedProperty = field.focusedProperty();

        field.setText(settingGetter.get().toString());
        textProperty.addListener((o, oldValue, newValue) -> processFieldChange(field, oldValue, newValue));
        focusedProperty.addListener((o, old, isFocused) -> processFocusChange(field, settingGetter));
    }

    private void processFocusChange(final TextField field, final Supplier<Integer> settingGetter) {
        String text = field.getText();

        if (text.isEmpty()) {
            field.setText(settingGetter.get().toString());
        }
    }

    private void processFieldChange(final TextField field, final String oldValue, final String newValue) {
        String clean = cleanInt(oldValue, newValue);

        if (!clean.equals(newValue)) {
            field.setText(clean);
        }
    }

    private String cleanInt(final String oldValue, final String newValue) {
        if (newValue.isEmpty()) {
            return newValue;
        } else {
            try {
                int n = Integer.parseInt(newValue);

                if (n >= 0) {
                    return Integer.toString(n);
                }
            } catch (NumberFormatException e) {
                LOG.debug("Illegal field value", e);
            }

            return oldValue;
        }
    }
}
