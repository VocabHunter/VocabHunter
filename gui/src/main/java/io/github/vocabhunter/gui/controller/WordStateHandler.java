/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static javafx.beans.binding.Bindings.notEqual;

public class WordStateHandler {
    private final Map<KeyCode, Runnable> keyHandlers = buildKeyHandlerMap();

    private SessionModel sessionModel;

    private Runnable nextWordSelector;

    public void initialise(final Button buttonUnseen, final Button buttonKnown, final Button buttonUnknown, final SessionModel sessionModel,
                            final ObjectBinding<WordState> wordStateProperty, final Runnable nextWordSelector) {
        this.sessionModel = sessionModel;
        this.nextWordSelector = nextWordSelector;

        SimpleBooleanProperty editableProperty = sessionModel.editableProperty();
        BooleanBinding resettableProperty = editableProperty.and(notEqual(WordState.UNSEEN, wordStateProperty));

        buttonUnseen.visibleProperty().bind(resettableProperty);
        buttonKnown.visibleProperty().bind(editableProperty);
        buttonUnknown.visibleProperty().bind(editableProperty);

        buttonUnseen.setOnAction(e -> processResponse(WordState.UNSEEN, false));
        buttonKnown.setOnAction(e -> processResponse(WordState.KNOWN, true));
        buttonUnknown.setOnAction(e -> processResponse(WordState.UNKNOWN, true));
    }

    public void processKeyPress(final KeyEvent event) {
        KeyCode key = event.getCode();

        if (keyHandlers.containsKey(key)) {
            event.consume();
            keyHandlers.get(key).run();
        }
    }

    private void processResponse(final WordState state, final boolean isSelectionChange) {
        WordModel word = sessionModel.getCurrentWord();

        if (state == WordState.UNKNOWN) {
            sessionModel.addSelectedWord(word);
        } else {
            sessionModel.removeDeselectedWord(word);
        }
        word.setState(state);
        if (isSelectionChange) {
            nextWordSelector.run();
        }
        sessionModel.setChangesSaved(false);
    }

    private Map<KeyCode, Runnable> buildKeyHandlerMap() {
        Map<KeyCode, Runnable> map = new EnumMap<>(KeyCode.class);

        map.put(KeyCode.K, () -> processResponse(WordState.KNOWN, true));
        map.put(KeyCode.X, () -> processResponse(WordState.UNKNOWN, true));
        map.put(KeyCode.R, () -> processResponse(WordState.UNSEEN, false));

        return Collections.unmodifiableMap(map);
    }
}
