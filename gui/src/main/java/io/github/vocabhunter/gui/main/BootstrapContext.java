/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.ThreadPoolToolImpl;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.FileListManagerImpl;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BootstrapContext {
    private static final Logger LOG = LoggerFactory.getLogger(BootstrapContext.class);

    private final long startupNanos;

    private CompletableFuture<Parent> futureRoot;

    private CompletableFuture<Scene> futureScene;

    private CompletableFuture<Void> futureSettingsLoad;

    private CompletableFuture<Void> futureFileListLoad;

    private SettingsManager settingsManager;

    private FileListManager fileListManager;

    public BootstrapContext(final long startupNanos) {
        this.startupNanos = startupNanos;
    }

    public void applyRootSupplier(final Supplier<Parent> supplier) {
        futureRoot = CompletableFuture.supplyAsync(supplier, ThreadPoolToolImpl.GUI_THREAD_POOL);
    }

    public CompletableFuture<Parent> getFutureRoot() {
        return futureRoot;
    }

    public void applySceneSupplier(final Supplier<Scene> supplier) {
        futureScene = CompletableFuture.supplyAsync(supplier, ThreadPoolToolImpl.GUI_THREAD_POOL);
    }

    public CompletableFuture<Scene> getFutureScene() {
        return futureScene;
    }

    public void startSettingsLoad() {
        futureSettingsLoad = CompletableFuture.supplyAsync(this::loadSettings, ThreadPoolToolImpl.GUI_THREAD_POOL);
        futureFileListLoad = CompletableFuture.supplyAsync(this::loadFileList, ThreadPoolToolImpl.GUI_THREAD_POOL);
    }

    private Void loadSettings() {
        SettingsManagerImpl settingsManagerImpl = new SettingsManagerImpl();

        settingsManagerImpl.initialise();
        settingsManager = settingsManagerImpl;

        return null;
    }

    private Void loadFileList() {
        FileListManagerImpl fileListManagerImpl = new FileListManagerImpl();

        fileListManagerImpl.initialise();
        fileListManager = fileListManagerImpl;

        return null;
    }

    public SettingsManager getSettingsManager() {
        futureSettingsLoad.join();

        return settingsManager;
    }

    public FileListManager getFileListManager() {
        futureFileListLoad.join();

        return fileListManager;
    }

    public void logStartup() {
        LOG.info("User interface started ({} ms)", CoreTool.millisToNow(startupNanos));
    }
}
