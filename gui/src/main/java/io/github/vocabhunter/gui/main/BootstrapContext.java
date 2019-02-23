/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.CoreTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapContext {
    private static final Logger LOG = LoggerFactory.getLogger(BootstrapContext.class);

    private final long startupNanos;

    public BootstrapContext(final long startupNanos) {
        this.startupNanos = startupNanos;
    }

    public void logStartup() {
        LOG.info("User interface started ({} ms)", CoreTool.millisToNow(startupNanos));
    }
}
