/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileToolTest {
    private static final String ERROR_TEMPLATE = "Error %s";
    private TestFileManager manager;

    private Path base;

    private Path existingFile;

    private Path newFile;

    private Path subDirectory;

    @Before
    public void setUp() throws Exception {
        manager = new TestFileManager(getClass());
        base = manager.getDirectory();
        existingFile = manager.addFile("existingFile");
        Files.createFile(existingFile);
        newFile = manager.addFile("newFile");
        subDirectory = manager.addFile("directory");
        Files.createDirectories(subDirectory);
    }

    @After
    public void tearDown() throws Exception {
        manager.cleanup();
    }

    @Test
    public void testEnsureDirectoryExistsWithDirectory() {
        FileTool.ensureDirectoryExists(existingFile, ERROR_TEMPLATE);

        assertTrue(Files.isDirectory(base));
    }

    @Test(expected = VocabHunterException.class)
    public void testEnsureDirectoryExistsWithError() {
        Path impossible = existingFile.resolve("file");

        FileTool.ensureDirectoryExists(impossible, ERROR_TEMPLATE);
    }

    @Test
    public void testEnsureDirectoryExistsWithoutBase() {
        Path file = Paths.get("file");
        FileTool.ensureDirectoryExists(file, ERROR_TEMPLATE);
    }

    @Test
    public void testWriteMinimalJsonSuccess() throws Exception {
        FileTool.writeMinimalJson(newFile, ERROR_TEMPLATE);

        List<String> result = Files.readAllLines(newFile);
        List<String> expected = Collections.singletonList("{}");

        assertEquals(expected, result);
    }

    @Test(expected = VocabHunterException.class)
    public void testWriteMinimalJsonFailure() throws Exception {
        FileTool.writeMinimalJson(subDirectory, ERROR_TEMPLATE);
    }

    @Test
    public void testReadThenWriteJson() {
        FileToolTestBean bean = new FileToolTestBean("left", "right");

        FileTool.writeAsJson(newFile, bean, ERROR_TEMPLATE);
        FileToolTestBean result = FileTool.readFromJson(FileToolTestBean.class, newFile, ERROR_TEMPLATE);

        assertEquals(bean, result);
    }

    @Test(expected = VocabHunterException.class)
    public void testWriteAsJsonWithError() {
        FileTool.writeAsJson(subDirectory, new FileToolTestBean(), ERROR_TEMPLATE);
    }

    @Test(expected = VocabHunterException.class)
    public void testReadFromJsonWithError() {
        FileTool.readFromJson(FileToolTestBean.class, subDirectory, ERROR_TEMPLATE);
    }
}
