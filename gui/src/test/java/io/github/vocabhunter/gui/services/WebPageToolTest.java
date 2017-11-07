/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebPageToolTest {
    private static final String WEB_PAGE = "WEB_PAGE";

    @Mock
    private ThreadPoolTool threadPoolTool;

    @Mock
    private ExecutorService executorService;

    @Mock
    private Consumer<String> pageOpener;

    @Captor
    private ArgumentCaptor<Runnable> captor;

    private WebPageTool target;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        when(threadPoolTool.singleDaemonExecutor(anyString())).thenReturn(executorService);
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
