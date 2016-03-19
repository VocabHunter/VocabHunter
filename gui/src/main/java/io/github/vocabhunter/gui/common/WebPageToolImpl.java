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

public final class WebPageToolImpl implements WebPageTool {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageToolImpl.class);

    private final ExecutorService executorService = ThreadPoolTool.singleDaemonExecutor("Web Page Opener");

    @Override
    public void showWebPage(final String page) {
        executorService.submit(() -> openPage(page));
    }

    private void openPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            LOG.error("Unable to open page {}", page, e);
        }
    }
}
