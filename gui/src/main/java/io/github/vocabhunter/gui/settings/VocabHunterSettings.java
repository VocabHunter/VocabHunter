/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

public class VocabHunterSettings {
    private String documentsPath;

    private String sessionsPath;

    private String exportPath;

    public String getDocumentsPath() {
        return documentsPath;
    }

    public void setDocumentsPath(final String documentsPath) {
        this.documentsPath = documentsPath;
    }

    public String getSessionsPath() {
        return sessionsPath;
    }

    public void setSessionsPath(final String sessionsPath) {
        this.sessionsPath = sessionsPath;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(final String exportPath) {
        this.exportPath = exportPath;
    }
}
