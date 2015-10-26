/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.factory.FileDialogueFactory;

public class TestGuiApplication extends VocabHunterGuiExecutable {
    private static FileDialogueFactory fileDialogueFactory;

    public static void setFileDialogueFactory(final FileDialogueFactory fileDialogueFactory) {
        TestGuiApplication.fileDialogueFactory = fileDialogueFactory;
    }

    public TestGuiApplication() {
        super(fileDialogueFactory);
    }
}
