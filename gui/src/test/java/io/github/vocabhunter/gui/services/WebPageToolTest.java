/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebPageToolTest {
    private static final String WEB_PAGE = "WEB_PAGE";

    private final I18nManager i18nManager = I18nManagerImpl.createForDefaultLocale();

    @Mock
    private Consumer<String> pageOpener;

    private WebPageTool target;

    @BeforeEach
    public void setUp() {
        target = new WebPageToolImpl(Runnable::run, pageOpener, i18nManager);
    }

    @Test
    public void testShowWebPage() {
        target.showWebPage(WEB_PAGE);
        verify(pageOpener).accept(WEB_PAGE);
    }

    @Test
    public void testShowWebPageWithLookup() {
        target.showWebPage(I18nKey.LINK_MAIN);
        verify(pageOpener).accept(GuiConstants.WEBSITE);
    }
}
