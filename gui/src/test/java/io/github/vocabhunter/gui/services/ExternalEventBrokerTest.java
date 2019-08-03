/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExternalEventBrokerTest {
    private static final Path FILE_1 = Paths.get("FILE_1");

    private static final Path FILE_2 = Paths.get("FILE_2");

    private final List<Path> files = new ArrayList<>();

    private final Consumer<Path> fileConsumer = files::add;

    private final ExternalEventBroker target = new ExternalEventBrokerImpl();

    @Test
    public void testEmpty() {
        validateFiles();
    }

    @Test
    public void testOpenWithoutSessionOrGui() {
        target.openFile(FILE_1);

        validateFiles();
    }

    @Test
    public void testOpenWithoutSession() {
        target.openFile(FILE_1);
        target.markGuiOpen(fileConsumer);

        validateFiles();
    }

    @Test
    public void testOpenWithoutGui() {
        target.openFile(FILE_1);
        target.markMainDisplayShown();

        validateFiles();
    }

    @Test
    public void testStandardOpenSequence() {
        target.openFile(FILE_1);
        validateFiles();

        target.markMainDisplayShown();
        validateFiles();

        target.markGuiOpen(fileConsumer);
        validateFiles(FILE_1);
    }

    @Test
    public void testReverseOpenSequence() {
        target.openFile(FILE_1);
        validateFiles();

        target.markGuiOpen(fileConsumer);
        validateFiles();

        target.markMainDisplayShown();
        validateFiles(FILE_1);
    }

    @Test
    public void testDuplicateGui() {
        target.openFile(FILE_1);
        validateFiles();

        target.markGuiOpen(fileConsumer);
        target.markGuiOpen(fileConsumer);
        validateFiles();

        target.markMainDisplayShown();
        validateFiles(FILE_1);
    }

    @Test
    public void testDuplicateSession() {
        target.openFile(FILE_1);
        validateFiles();

        target.markGuiOpen(fileConsumer);
        validateFiles();

        target.markMainDisplayShown();
        target.markMainDisplayShown();
        validateFiles(FILE_1);
    }

    @Test
    public void testMultipleFiles() {
        target.openFile(FILE_1);
        validateFiles();

        target.markGuiOpen(fileConsumer);
        validateFiles();

        target.markMainDisplayShown();
        validateFiles(FILE_1);

        target.openFile(FILE_2);
        validateFiles(FILE_1, FILE_2);
    }

    private void validateFiles(final Path... expected) {
        assertEquals(List.of(expected), files);
    }
}
