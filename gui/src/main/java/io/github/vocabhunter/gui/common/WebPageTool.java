/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.util.concurrent.ExecutorService;

public final class WebPageTool {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageTool.class);

    private static ExecutorService executorService = ThreadPoolTool.singleDaemonExecutor("Web Page Opener");

    private WebPageTool() {
        // Prevent instantiation - all methods are static
    }

    public static void showWebPage(final String page) {
        executorService.submit(() -> openPage(page));
    }

    private static void openPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            LOG.error("Unable to open page {}", page, e);
        }
    }
}
