/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.ThreadPoolToolImpl;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BootstrapContext {
    private static final Logger LOG = LoggerFactory.getLogger(BootstrapContext.class);

    private final long startupNanos;

    private CompletableFuture<Parent> futureRoot;

    public BootstrapContext(final long startupNanos) {
        this.startupNanos = startupNanos;
    }

    public void applyRootSupplier(final Supplier<Parent> supplier) {
        futureRoot = CompletableFuture.supplyAsync(supplier, ThreadPoolToolImpl.GUI_THREAD_POOL);
    }

    public CompletableFuture<Parent> getFutureRoot() {
        return futureRoot;
    }

    public void logStartup() {
        LOG.info("User interface started ({} ms)", CoreTool.millisToNow(startupNanos));
    }
}
