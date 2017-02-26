/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import java.nio.file.Path;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class FileListManagerImpl extends BaseSettingsManager<FileList> implements FileListManager {
    public static final String SETTINGS_JSON = "file-list.json";

    public FileListManagerImpl() {
        super(SETTINGS_JSON, FileList.class);
    }

    public FileListManagerImpl(final Path settingsFile) {
        super(settingsFile, FileList.class);
    }

    @Override
    public List<BaseListedFile> getFilterFiles() {
        FileList bean = readSettings();

        return bean.getFilterFiles();
    }

    @Override
    public void setFilterFiles(final List<BaseListedFile> files) {
        FileList bean = readSettings();

        bean.setFilterFiles(files);
        writeSettings(bean);
    }
}
