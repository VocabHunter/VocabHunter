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

import static java.util.stream.Collectors.toList;

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

        return new SessionModel(state.getName(), words(state, progressModel), filterSettings, progressModel, positionModel, windowSettings);
    }

    private List<WordModel> words(final SessionState raw, final ProgressModel progressModel) {
        List<String> lines = raw.getLines();
        List<SessionWord> orderedUses = raw.getOrderedUses();
        int useCount = orderedUses.size();

        return IntStream.range(0, useCount)
                .mapToObj(n -> wordModel(lines, n, orderedUses.get(n), progressModel))
                .collect(toList());
    }

    private WordModel wordModel(final List<String> lines, final int n, final SessionWord word, final ProgressModel progressModel) {
        List<String> uses = word.getLineNos().stream()
            .map(lines::get)
            .collect(toList());
        WordModel model = new WordModel(n, word.getWordIdentifier(), uses, word.getUseCount(), word.getState(), toGuiNote(word.getNote()));

        model.stateProperty().addListener((o, old, s) -> word.setState(s));
        model.noteProperty().addListener((o, old, s) -> word.setNote(fromGuiNote(s)));
        model.stateProperty().addListener((o, old, s) -> progressModel.updateWord(old, s));

        return model;
    }

    private String toGuiNote(final String note) {
        if (note == null) {
            return "";
        } else {
            return note;
        }
    }

    private String fromGuiNote(final String note) {
        if ("".equals(note)) {
            return null;
        } else {
            return note;
        }
    }
}
