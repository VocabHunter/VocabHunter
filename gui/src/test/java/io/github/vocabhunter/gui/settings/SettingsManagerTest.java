/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.vocabhunter.gui.settings.SettingsManagerImpl.SETTINGS_JSON;
import static io.github.vocabhunter.gui.settings.VocabHunterSettings.*;
import static org.junit.Assert.*;

public class SettingsManagerTest {
    private static final int UPDATE_INT_VALUE = 12345;

    private final Path home = Paths.get(System.getProperty("user.home"));

    private final WindowSettings windowSettings = buildWindowSettings();

    private TestFileManager files;

    private Path dummyPath;

    private SettingsManager target;

    @Before
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        Path settingsFile = files.addFile(SETTINGS_JSON);
        dummyPath = files.addFile("dummy");
        target = new SettingsManagerImpl(settingsFile);
    }

    @After
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testGetDefaultDocumentsPath() {
        validateGetDefaultPath(target::getDocumentsPath);
    }

    @Test
    public void testUpdateDocumentsPath() throws Exception {
        validateUpdatePath(target::getDocumentsPath, target::setDocumentsPath);
    }

    @Test
    public void testMissingDocumentsPath() {
        validateMissingPath(target::getDocumentsPath, target::setDocumentsPath);
    }

    @Test
    public void testGetDefaultSessionsPath() {
        validateGetDefaultPath(target::getSessionsPath);
    }

    @Test
    public void testUpdateSessionsPath() throws Exception {
        validateUpdatePath(target::getSessionsPath, target::setSessionsPath);
    }

    @Test
    public void testMissingSessionsPath() {
        validateMissingPath(target::getSessionsPath, target::setSessionsPath);
    }

    @Test
    public void testGetDefaultExportPath() {
        validateGetDefaultPath(target::getExportPath);
    }

    @Test
    public void testUpdateExportPath() throws Exception {
        validateUpdatePath(target::getExportPath, target::setExportPath);
    }

    @Test
    public void testMissingExportPath() {
        validateMissingPath(target::getExportPath, target::setExportPath);
    }

    @Test
    public void testGetDefaultWordListPath() {
        validateGetDefaultPath(target::getWordListPath);
    }

    @Test
    public void testUpdateWordListPath() throws Exception {
        validateUpdatePath(target::getWordListPath, target::setWordListPath);
    }

    @Test
    public void testMissingWordListPath() {
        validateMissingPath(target::getWordListPath, target::setWordListPath);
    }

    @Test
    public void testUpdateFilterMinimumLetters() {
        validateUpdateInt(target::getFilterMinimumLetters, target::setFilterMinimumLetters);
    }

    @Test
    public void testMissingFilterMinimumLetters() {
        validateMissingInt(target::getFilterMinimumLetters, DEFAULT_MINIMUM_LETTERS);
    }

    @Test
    public void testUpdateFilterMinimumOccurrences() {
        validateUpdateInt(target::getFilterMinimumOccurrences, target::setFilterMinimumOccurrences);
    }

    @Test
    public void testMissingFilterMinimumOccurrences() {
        validateMissingInt(target::getFilterMinimumOccurrences, DEFAULT_MINIMUM_OCCURRENCES);
    }

    @Test
    public void testUpdateAllowInitialCapitals() {
        target.setAllowInitialCapitals(false);
        assertFalse("Disallow initial capital", target.isAllowInitialCapitals());

        target.setAllowInitialCapitals(true);
        assertTrue("Allow initial capital", target.isAllowInitialCapitals());
    }

    @Test
    public void testMissingAllowInitialCapitals() {
        assertEquals("Missing initial capital", DEFAULT_ALLOW_INITIAL_CAPITALS, target.isAllowInitialCapitals());
    }

    @Test
    public void testMissingWindowSettings() {
        validateEmpty(target::getWindowSettings);
    }

    @Test
    public void testUpdateWindowSettings() {
        validateOptional(target::getWindowSettings, target::setWindowSettings, windowSettings, windowSettings);
    }

    private void validateGetDefaultPath(final Supplier<Path> getter) {
        Path path = getter.get();

        assertEquals("Default path", home, path);
    }

    private void validateUpdatePath(final Supplier<Path> getter, final Consumer<Path> setter) throws Exception {
        Files.createDirectories(dummyPath);
        validate(getter, setter, dummyPath);
    }

    private void validateMissingPath(final Supplier<Path> getter, final Consumer<Path> setter) {
        validate(getter, setter, home);
    }

    private <T> void validateOptional(final Supplier<Optional<T>> getter, final Consumer<T> setter, final T expected, final T value) {
        validate(() -> getter.get().get(), setter, expected, value);
    }

    private void validate(final Supplier<Path> getter, final Consumer<Path> setter, final Path expected) {
        Path value = dummyPath;
        validate(getter, setter, expected, value);
    }

    private <T> void validate(final Supplier<T> getter, final Consumer<T> setter, final T expected, final T value) {
        setter.accept(value);
        T path = getter.get();

        assertEquals("Saved value", expected, path);
    }

    private void validateMissingInt(final Supplier<Integer> getter, final int expected) {
        int actual = getter.get();

        assertEquals("Default int", expected, actual);
    }

    private void validateUpdateInt(final Supplier<Integer> getter, final Consumer<Integer> setter) {
        setter.accept(UPDATE_INT_VALUE);
        int actual = getter.get();

        assertEquals("Updated int", UPDATE_INT_VALUE, actual);
    }

    private void validateEmpty(final Supplier<Optional<?>> getter) {
        Optional<?> o = getter.get();

        assertFalse("Empty", o.isPresent());
    }

    private WindowSettings buildWindowSettings() {
        WindowSettings settings = new WindowSettings();

        settings.setX(1);
        settings.setY(2);
        settings.setWidth(3);
        settings.setHeight(4);
        settings.setSplitUsePosition(5);
        settings.setSplitWordPosition(6);

        return settings;
    }
}
