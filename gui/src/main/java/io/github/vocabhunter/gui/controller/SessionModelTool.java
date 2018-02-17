/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.gui.model.*;
import io.github.vocabhunter.gui.settings.WindowSettings;
import io.github.vocabhunter.gui.view.SessionTab;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.stream.IntStream;

import static io.github.vocabhunter.gui.common.GuiNoteTool.fromGuiNote;

public class SessionModelTool {
    private final SessionState state;

    private final FilterSettings filterSettings;

    private final SimpleObjectProperty<SessionTab> tabProperty;

    private final WindowSettings windowSettings;

    public SessionModelTool(final SessionState state, final FilterSettings filterSettings, final SimpleObjectProperty<SessionTab> tabProperty, final WindowSettings windowSettings) {
        this.state = state;
        this.filterSettings = filterSettings;
        this.tabProperty = tabProperty;
        this.windowSettings = windowSettings;
    }

    public SessionModel buildModel() {
        ProgressModel progressModel = new ProgressModel();
        PositionModel positionModel = new PositionModel();

        positionModel.analysisModeProperty().bind(Bindings.createBooleanBinding(() -> tabProperty.get().equals(SessionTab.ANALYSIS), tabProperty));

        return new SessionModel(state.getName(), state.getLines(), words(state, progressModel), filterSettings, progressModel, positionModel, windowSettings);
    }

    private List<WordModel> words(final SessionState raw, final ProgressModel progressModel) {
        List<SessionWord> orderedUses = raw.getOrderedUses();
        int useCount = orderedUses.size();
        List<WordModel> wordModels = WordModelTool.wordModels(raw);

        IntStream.range(0, useCount)
            .forEach(i -> addListeners(orderedUses, wordModels, i, progressModel));

        return wordModels;
    }

    private void addListeners(final List<SessionWord> orderedUses, final List<WordModel> wordModels, final int index, final ProgressModel progressModel) {
        SessionWord sessionWord = orderedUses.get(index);
        WordModel wordModel = wordModels.get(index);

        wordModel.stateProperty().addListener((o, old, s) -> sessionWord.setState(s));
        wordModel.noteProperty().addListener((o, old, s) -> sessionWord.setNote(fromGuiNote(s)));
        wordModel.stateProperty().addListener((o, old, s) -> progressModel.updateWord(old, s));
    }
}
