/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import io.github.vocabhunter.gui.model.MainModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TitleHandlerTest {
    private static final Path SESSION_FILE = Paths.get("root", "saved.file");

    private static final String DOCUMENT_NAME = "My Document";

    private final MainModel model = new MainModel();

    private final I18nManager i18nManager = I18nManagerImpl.createForDefaultLocale();

    private final TitleHandler target = new TitleHandler(model, i18nManager);

    @BeforeEach
    public void setUp() {
        target.initialise();
    }

    @Test
    public void testInitial() {
        validate("VocabHunter");
    }

    @Test
    public void testInitialUnsaved() {
        model.setSessionOpen(true);
        model.changesSavedProperty().set(false);
        validate("Untitled - Unsaved Changes - VocabHunter");
    }

    @Test
    public void testSessionFile() {
        model.setSessionOpen(true);
        model.sessionFileProperty().set(SESSION_FILE);
        validate("saved.file - VocabHunter");
    }

    @Test
    public void testSessionFileUnsaved() {
        model.setSessionOpen(true);
        model.sessionFileProperty().set(SESSION_FILE);
        model.changesSavedProperty().set(false);
        validate("saved.file - Unsaved Changes - VocabHunter");
    }

    @Test
    public void testDocumentName() {
        model.setSessionOpen(true);
        model.documentNameProperty().set(DOCUMENT_NAME);
        validate("Untitled (My Document) - VocabHunter");
    }

    @Test
    public void testDocumentNameUnsaved() {
        model.setSessionOpen(true);
        model.documentNameProperty().set(DOCUMENT_NAME);
        model.changesSavedProperty().set(false);
        validate("Untitled (My Document) - Unsaved Changes - VocabHunter");
    }

    @Test
    public void testSessionFileDocumentName() {
        model.setSessionOpen(true);
        model.sessionFileProperty().set(SESSION_FILE);
        model.documentNameProperty().set(DOCUMENT_NAME);
        validate("saved.file (My Document) - VocabHunter");
    }

    @Test
    public void testSessionFileDocumentNameUnsaved() {
        model.setSessionOpen(true);
        model.sessionFileProperty().set(SESSION_FILE);
        model.documentNameProperty().set(DOCUMENT_NAME);
        model.changesSavedProperty().set(false);
        validate("saved.file (My Document) - Unsaved Changes - VocabHunter");
    }

    private void validate(final String expected) {
        String actual = model.titleProperty().get();

        assertEquals(expected, actual, "Title");
    }
}
