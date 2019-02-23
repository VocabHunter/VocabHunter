/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabHunterBootstrap {
    private final VocabHunterGui vocabHunterGui;

    private final FXMLLoader mainLoader;

    @Inject
    public VocabHunterBootstrap(final VocabHunterGui vocabHunterGui, final FXMLLoader mainLoader) {
        this.vocabHunterGui = vocabHunterGui;
        this.mainLoader = mainLoader;
    }

    public void start(final Stage stage, final BootstrapContext bootstrapContext) {
        bootstrapContext.applyRootSupplier(() -> ViewFxml.MAIN.loadNode(mainLoader));
        vocabHunterGui.start(stage, bootstrapContext);
    }
}
