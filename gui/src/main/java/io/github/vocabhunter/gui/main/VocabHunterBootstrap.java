/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabHunterBootstrap {
    private final VocabHunterGui vocabHunterGui;

    private final FXMLLoader mainLoader;

    private final ThreadPoolTool threadPoolTool;

    @Inject
    public VocabHunterBootstrap(final VocabHunterGui vocabHunterGui, final FXMLLoader mainLoader, final ThreadPoolTool threadPoolTool) {
        this.vocabHunterGui = vocabHunterGui;
        this.mainLoader = mainLoader;
        this.threadPoolTool = threadPoolTool;
    }

    public void start(final Stage stage, final BootstrapContext bootstrapContext) {
        CompletableFuture<Parent> futureRoot = CompletableFuture.supplyAsync(() -> ViewFxml.MAIN.loadNode(mainLoader), threadPoolTool.guiThreadPool());

        vocabHunterGui.start(stage, bootstrapContext, futureRoot);
    }
}
