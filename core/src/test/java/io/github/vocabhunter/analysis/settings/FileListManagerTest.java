/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static io.github.vocabhunter.analysis.settings.FileListManagerImpl.SETTINGS_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileListManagerTest {
    private TestFileManager files;

    private SessionListedFile file1;

    private DocumentListedFile file2;

    private ExcelListedFile file3;

    private FileListManager target;

    @BeforeEach
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        Path settingsFile = files.addFile(SETTINGS_JSON);
        target = new FileListManagerImpl(settingsFile);

        Path path1 = files.addFile("file1");
        file1 = new SessionListedFile(path1, true);

        Path path2 = files.addFile("file2");
        file2 = new DocumentListedFile(path2);

        Path path3 = files.addFile("file3");
        file3 = new ExcelListedFile(path3, Arrays.asList(1, 2, 3));
    }

    @AfterEach
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testEmpty() {
        List<BaseListedFile> actual = target.getFilterFiles();

        assertTrue(actual.isEmpty(), "Empty file list");
    }

    @Test
    public void testAdd() {
        List<BaseListedFile> files = CoreTool.listOf(file1, file2, file3);

        target.setFilterFiles(files);

        List<BaseListedFile> actual = target.getFilterFiles();

        assertEquals(files, actual, "Files");
    }
}
