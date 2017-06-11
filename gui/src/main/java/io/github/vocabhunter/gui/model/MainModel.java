/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.session.SessionState;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Path;
import java.util.Optional;
import javax.inject.Singleton;

import static javafx.beans.binding.Bindings.isNotEmpty;

@Singleton
public class MainModel {
    private final SimpleStringProperty title = new SimpleStringProperty();

    private SessionState sessionState;

    private SessionModel sessionModel;

    private final SimpleBooleanProperty sessionOpen = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty selectionAvailable = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty editMode = new SimpleBooleanProperty(true);

    private final SimpleObjectProperty<Path> sessionFile = new SimpleObjectProperty<>(null);

    private final SimpleStringProperty documentName = new SimpleStringProperty();

    private final SimpleBooleanProperty changesSaved = new SimpleBooleanProperty(true);

    private final SimpleObjectProperty<FilterSettings> filterSettings = new SimpleObjectProperty<>();

    private final SimpleBooleanProperty enableFilters = new SimpleBooleanProperty(true);

    private WordFilter filter;

    public void replaceSessionModel(final SessionState sessionState, final SessionModel sessionModel, final Path sessionFile) {
        unbindOldSession();

        this.sessionState = sessionState;
        this.sessionModel = sessionModel;
        this.sessionFile.set(sessionFile);
        sessionOpen.set(true);
        selectionAvailable.bind(isNotEmpty(sessionModel.getSelectedWords()));
        editMode.bindBidirectional(sessionModel.editableProperty());
        sessionModel.filterSettingsProperty().bindBidirectional(filterSettings);
        sessionModel.enableFiltersProperty().bindBidirectional(enableFilters);
        documentName.bind(sessionModel.documentNameProperty());
        changesSaved.bindBidirectional(sessionModel.changesSavedProperty());
    }

    private void unbindOldSession() {
        selectionAvailable.unbind();
        documentNameProperty().unbind();
        if (sessionState != null) {
            editMode.unbindBidirectional(sessionModel.editableProperty());
            changesSaved.unbindBidirectional(sessionModel.changesSavedProperty());
            sessionModel.filterSettingsProperty().unbindBidirectional(filterSettings);
            sessionModel.enableFiltersProperty().unbindBidirectional(enableFilters);
        }
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

    public SimpleObjectProperty<FilterSettings> filterSettingsProperty() {
        return filterSettings;
    }

    public void setFilterSettings(final FilterSettings filterSettings) {
        this.filterSettings.set(filterSettings);
    }

    public FilterSettings getFilterSettings() {
        return filterSettings.get();
    }

    public void setFilter(final WordFilter filter) {
        this.filter = filter;
    }

    public WordFilter getFilter() {
        return filter;
    }

    public SimpleBooleanProperty enableFiltersProperty() {
        return enableFilters;
    }

    public void setEnableFilters(final boolean enableFilters) {
        this.enableFilters.set(enableFilters);
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

    public Optional<SessionState> getSessionState() {
        return Optional.ofNullable(sessionState);
    }

    public Optional<SessionModel> getSessionModel() {
        return Optional.ofNullable(sessionModel);
    }
}
