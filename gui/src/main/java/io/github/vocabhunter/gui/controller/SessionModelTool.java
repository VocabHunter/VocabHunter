/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SessionModelTool {
    private final SessionState state;

    private final FilterSettings filterSettings;

    public SessionModelTool(final SessionState state, final FilterSettings filterSettings) {
        this.state = state;
        this.filterSettings = filterSettings;
    }

    public SessionModel buildModel() {
        ProgressModel progressModel = new ProgressModel();

        return new SessionModel(state.getName(), words(state, progressModel), filterSettings, progressModel);
    }

    private List<WordModel> words(final SessionState raw, final ProgressModel progressModel) {
        List<SessionWord> orderedUses = raw.getOrderedUses();
        int useCount = orderedUses.size();

        return IntStream.range(0, useCount)
                .mapToObj(n -> wordModel(n, orderedUses.get(n), progressModel))
                .collect(Collectors.toList());
    }

    private WordModel wordModel(final int n, final SessionWord word, final ProgressModel progressModel) {
        WordModel model = new WordModel(n, word.getWordIdentifier(), word.getUses(), word.getUseCount(), word.getState());

        model.stateProperty().addListener((o, old, s) -> word.setState(s));
        model.stateProperty().addListener((o, old, s) -> progressModel.updateWord(old, s));

        return model;
    }
}
