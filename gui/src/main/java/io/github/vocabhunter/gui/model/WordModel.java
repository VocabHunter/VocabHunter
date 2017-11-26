/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.common.SequencedWord;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

import java.util.Collections;
import java.util.List;

public class WordModel implements MarkedWord, SequencedWord {
    public static final Callback<WordModel, Observable[]> PROPERTY_EXTRACTOR
            = w -> new Observable[] {w.identifier, w.state};

    private final int sequenceNo;

    private final List<String> uses;

    private final int useCount;

    private final SimpleStringProperty identifier;

    private final SimpleObjectProperty<WordState> state;

    private final SimpleStringProperty note;

    public WordModel(final int sequenceNo, final String word, final List<String> uses, final int useCount, final WordState state, final String note) {
        this.uses = uses;
        this.useCount = useCount;
        this.identifier = new SimpleStringProperty(word);
        this.sequenceNo = sequenceNo;
        this.state = new SimpleObjectProperty<>(state);
        this.note = new SimpleStringProperty(note);
    }

    @Override
    public int getSequenceNo() {
        return sequenceNo;
    }

    @Override
    public String getWordIdentifier() {
        return identifier.get();
    }

    @Override
    public WordState getState() {
        return state.get();
    }

    public void setState(final WordState state) {
        this.state.set(state);
    }

    public SimpleObjectProperty<WordState> stateProperty() {
        return state;
    }

    public List<String> getUses() {
        return Collections.unmodifiableList(uses);
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(final String note) {
        this.note.set(note);
    }
}
