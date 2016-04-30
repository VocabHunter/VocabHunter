/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import java.nio.file.Path;
import java.util.List;

public class FileListManagerImpl extends BaseSettingsManager<FileList> implements FileListManager {
    public static final String SETTINGS_JSON = "file-list.json";

    public FileListManagerImpl() {
        super(SETTINGS_JSON, FileList.class);
    }

    public FileListManagerImpl(final Path settingsFile) {
        super(settingsFile, FileList.class);
    }

    @Override
    public List<ListedFile> getFilteredSessionFiles() {
        FileList bean = readSettings();

        return bean.getFilterFiles();
    }

    @Override
    public void setFilteredSessionFiles(final List<ListedFile> files) {
        FileList bean = readSettings();

        bean.setFilterFiles(files);
        writeSettings(bean);
    }
}
