/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import javafx.beans.value.ObservableStringValue;

import javax.inject.Inject;
import javax.inject.Singleton;

import static javafx.beans.binding.Bindings.createStringBinding;

@Singleton
public class PositionDescriptionTool {
    private final I18nManager i18nManager;

    @Inject
    public PositionDescriptionTool(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public ObservableStringValue createBinding(final PositionModel position, final ProgressModel progress) {
        return createStringBinding(
            () -> describe(position, progress),
            position.positionIndexProperty(),
            position.sizeProperty(),
            position.analysisModeProperty(),
            position.editableProperty(),
            progress.unseenFilteredProperty());
    }

    private String describe(final PositionModel position, final ProgressModel progress) {
        if (position.isAnalysisMode()) {
            int wordNumber = position.getPositionIndex() + 1;
            int size = position.getSize();
            if (position.isEditable()) {
                int filtered = progress.unseenFilteredProperty().get();

                return i18nManager.text(I18nKey.STATUS_POSITION_EDIT_ON, wordNumber, size, filtered);
            } else {
                return i18nManager.text(I18nKey.STATUS_POSITION_EDIT_OFF, wordNumber, size);
            }
        } else {
            return "";
        }
    }
}
