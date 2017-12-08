/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.StateClassTool;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static javafx.beans.binding.Bindings.selectString;

public class MainWordHandler {
    private final Label mainWord;

    private final Label useCountLabel;

    private final Pane mainWordPane;

    private final SessionModel sessionModel;

    private final ObjectBinding<WordState> wordStateProperty;

    public MainWordHandler(final Label mainWord, final Label useCountLabel, final Pane mainWordPane, final SessionModel sessionModel, final ObjectBinding<WordState> wordStateProperty) {
        this.mainWord = mainWord;
        this.useCountLabel = useCountLabel;
        this.mainWordPane = mainWordPane;
        this.sessionModel = sessionModel;
        this.wordStateProperty = wordStateProperty;
    }

    public void prepare() {
        SimpleObjectProperty<WordModel> currentWordProperty = sessionModel.currentWordProperty();

        currentWordProperty.addListener((o, old, word) -> processWordUpdate(word));
        mainWord.textProperty().bind(selectString(currentWordProperty, "wordIdentifier"));
        currentWordProperty.addListener(o -> updateMainWordStateClasses());
        wordStateProperty.addListener((o, old, state) -> updateMainWordStateClasses());
        useCountLabel.textProperty().bind(sessionModel.useCountProperty());
        updateMainWordStateClasses();
    }

    public void processWordUpdate(final WordModel word) {
        if (word != null) {
            sessionModel.processWordUpdate(word);
        }
    }

    private void updateMainWordStateClasses() {
        StateClassTool.updateStateClasses(mainWordPane, sessionModel.getCurrentWord().getState());
    }
}
