/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SessionModelTool {
    private final SessionState state;

    public SessionModelTool(final SessionState state) {
        this.state = state;
    }

    public SessionModel buildModel() {
        return new SessionModel(state.getName(), words(state));
    }

    private List<WordModel> words(final SessionState raw) {
        List<SessionWord> orderedUses = raw.getOrderedUses();
        int useCount = orderedUses.size();

        return IntStream.range(0, useCount)
                .mapToObj(n -> wordModel(n, orderedUses.get(n)))
                .collect(Collectors.toList());
    }

    private WordModel wordModel(final int n, final SessionWord word) {
        WordModel model = new WordModel(n, word.getWordIdentifier(), word.getUses(), word.getState());

        model.stateProperty().addListener((o, old, s) -> word.setState(s));

        return model;
    }
}
