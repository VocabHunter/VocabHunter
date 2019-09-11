/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
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

    private final I18nManager i18nManager;

    @Inject
    public WebPageToolImpl(final ThreadPoolTool threadPoolTool, final I18nManager i18nManager) {
        this(threadPoolTool.guiThreadPool(), WebPageToolImpl::openPage, i18nManager);
    }

    public WebPageToolImpl(final Executor executor, final Consumer<String> pageOpener, final I18nManager i18nManager) {
        this.executor = executor;
        this.pageOpener = pageOpener;
        this.i18nManager = i18nManager;
    }

    @Override
    public void showWebPage(final String page) {
        executor.execute(() -> pageOpener.accept(page));
    }

    @Override
    public void showWebPage(final I18nKey key) {
        showWebPage(i18nManager.text(key));
    }

    private static void openPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            LOG.error("Unable to open page {}", page, e);
        }
    }
}
