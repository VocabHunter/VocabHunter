/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebPageToolImpl implements WebPageTool {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageToolImpl.class);

    private final Executor executor;

    private final Consumer<String> pageOpener;

    @Inject
    public WebPageToolImpl(final ThreadPoolTool threadPoolTool) {
        this(threadPoolTool, WebPageToolImpl::openPage);
    }

    public WebPageToolImpl(final ThreadPoolTool threadPoolTool, final Consumer<String> pageOpener) {
        this.executor = threadPoolTool.singleDaemonExecutor("Web Page Opener");
        this.pageOpener = pageOpener;
    }

    @Override
    public void showWebPage(final String page) {
        executor.execute(() -> pageOpener.accept(page));
    }

    private static void openPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            LOG.error("Unable to open page {}", page, e);
        }
    }
}
