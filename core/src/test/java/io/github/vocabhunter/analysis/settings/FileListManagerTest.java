/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.analysis.core.CollectionTool;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static io.github.vocabhunter.analysis.settings.FileListManagerImpl.SETTINGS_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileListManagerTest {
    private TestFileManager files;

    private ListedFile file1;

    private ListedFile file2;

    private FileListManager target;

    @Before
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        Path settingsFile = files.addFile(SETTINGS_JSON);
        target = new FileListManagerImpl(settingsFile);

        Path path1 = files.addFile("file1");
        file1 = new ListedFile(path1, ListedFileType.SESSION, true);

        Path path2 = files.addFile("file2");
        file2 = new ListedFile(path2, ListedFileType.SESSION, false);
    }

    @After
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testEmpty() {
        List<ListedFile> actual = target.getFilteredSessionFiles();

        assertTrue("Empty file list", actual.isEmpty());
    }

    @Test
    public void testAdd() {
        List<ListedFile> files = CollectionTool.listOf(file1, file2);

        target.setFilteredSessionFiles(files);

        List<ListedFile> actual = target.getFilteredSessionFiles();

        assertEquals("Files", files, actual);
    }
}
