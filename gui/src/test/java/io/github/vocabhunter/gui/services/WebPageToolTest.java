/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebPageToolTest {
    private static final String WEB_PAGE = "WEB_PAGE";

    @Mock
    private ThreadPoolTool threadPoolTool;

    @Mock
    private ScheduledExecutorService executorService;

    @Mock
    private Consumer<String> pageOpener;

    @Captor
    private ArgumentCaptor<Runnable> captor;

    private WebPageTool target;

    @BeforeEach
    public void setUp() {
        when(threadPoolTool.guiThreadPool()).thenReturn(executorService);
        target = new WebPageToolImpl(threadPoolTool, pageOpener);
    }

    @Test
    public void testShowWebPage() {
        target.showWebPage(WEB_PAGE);
        verify(executorService).execute(captor.capture());
        captor.getValue().run();
        verify(pageOpener).accept(WEB_PAGE);
    }
}
