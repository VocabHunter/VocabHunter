/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.SessionState;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Path;
import java.util.Optional;

import static javafx.beans.binding.Bindings.isNotEmpty;

public class MainModel {
    private final SimpleStringProperty title = new SimpleStringProperty();

    private Optional<SessionState> sessionState = Optional.empty();

    private Optional<SessionModel> sessionModel = Optional.empty();

    private final SimpleBooleanProperty sessionOpen = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty selectionAvailable = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty editMode = new SimpleBooleanProperty(true);

    private final SimpleObjectProperty<Path> sessionFile = new SimpleObjectProperty<>(null);

    private final SimpleStringProperty documentName = new SimpleStringProperty();

    private final SimpleBooleanProperty changesSaved = new SimpleBooleanProperty(true);

    public void replaceSessionModel(final SessionState sessionState, final SessionModel sessionModel) {
        replaceSessionModel(sessionState, sessionModel, null);
    }

    public void replaceSessionModel(final SessionState sessionState, final SessionModel sessionModel, final Path sessionFile) {
        unbindOldSession();

        this.sessionState = Optional.of(sessionState);
        this.sessionModel = Optional.of(sessionModel);
        this.sessionFile.set(sessionFile);
        sessionOpen.set(true);
        selectionAvailable.bind(isNotEmpty(sessionModel.getSelectedWords()));
        editMode.bindBidirectional(sessionModel.editableProperty());
        documentName.bind(sessionModel.documentNameProperty());
        changesSaved.bindBidirectional(sessionModel.changesSavedProperty());
    }

    private void unbindOldSession() {
        selectionAvailable.unbind();
        documentNameProperty().unbind();
        sessionModel.ifPresent(m -> {
            editMode.unbindBidirectional(m.editableProperty());
            changesSaved.unbindBidirectional(m.changesSavedProperty());
        });
    }

    public SimpleObjectProperty<Path> sessionFileProperty() {
        return sessionFile;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(final String title) {
        this.title.set(title);
    }

    public SimpleStringProperty documentNameProperty() {
        return documentName;
    }

    public SimpleBooleanProperty changesSavedProperty() {
        return changesSaved;
    }

    public void setChangesSaved(final boolean changesSaved) {
        this.changesSaved.set(changesSaved);
    }

    public boolean isChangesSaved() {
        return changesSaved.get();
    }

    public SimpleBooleanProperty sessionOpenProperty() {
        return sessionOpen;
    }

    public SimpleBooleanProperty selectionAvailableProperty() {
        return selectionAvailable;
    }

    public SimpleBooleanProperty editModeProperty() {
        return editMode;
    }

    public void setSessionFile(final Path sessionFile) {
        this.sessionFile.set(sessionFile);
    }

    public boolean hasSessionFile() {
        return sessionFile.getValue() != null;
    }

    public Path getSessionFile() {
        return sessionFile.get();
    }

    public SessionState getSessionState() {
        return sessionState.orElseThrow(() -> new VocabHunterException("No session state available"));
    }
}
