/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.FilterTool;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SessionModelTool {
    private final SessionState state;

    private final FilterSettings filterSettings;

    private final boolean isFilterRequested;

    private final boolean isFilterEnabled;

    public SessionModelTool(final SessionState state, final FilterSettings filterSettings, final boolean isFilterRequested) {
        this.state = state;
        this.filterSettings = filterSettings;
        this.isFilterRequested = isFilterRequested;
        this.isFilterEnabled = isFilterRequested && FilterTool.isValid(filterSettings, state.getOrderedUses());
    }

    public SessionModel buildModel() {
        return new SessionModel(state.getName(), words(state), filterSettings, isFilterEnabled);
    }

    public boolean isFilterError() {
        return isFilterRequested && !isFilterEnabled;
    }

    private List<WordModel> words(final SessionState raw) {
        List<SessionWord> orderedUses = raw.getOrderedUses();
        int useCount = orderedUses.size();

        return IntStream.range(0, useCount)
                .mapToObj(n -> wordModel(n, orderedUses.get(n)))
                .collect(Collectors.toList());
    }

    private WordModel wordModel(final int n, final SessionWord word) {
        WordModel model = new WordModel(n, word.getWordIdentifier(), word.getUses(), word.getUseCount(), word.getState());

        model.stateProperty().addListener((o, old, s) -> word.setState(s));

        return model;
    }
}
