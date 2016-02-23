/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.factory.FileDialogueFactory;
import io.github.vocabhunter.gui.settings.SettingsManager;

public class TestGuiApplication extends VocabHunterGuiExecutable {
    private static SettingsManager settingsManager;

    private static FileDialogueFactory fileDialogueFactory;

    public static void setSettingsManager(final SettingsManager settingsManager) {
        TestGuiApplication.settingsManager = settingsManager;
    }

    public static void setFileDialogueFactory(final FileDialogueFactory fileDialogueFactory) {
        TestGuiApplication.fileDialogueFactory = fileDialogueFactory;
    }

    public TestGuiApplication() {
        super(settingsManager, fileDialogueFactory);
    }
}
