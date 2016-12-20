/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
    public void setUp() {
        when(threadPoolTool.singleDaemonExecutor(anyString())).thenReturn(executorService);
        target = new WebPageToolImpl(threadPoolTool, pageOpener);
    }

    @Test
    public void testShowWebPage() {
        target.showWebPage(WEB_PAGE);
        verify(executorService).submit(captor.capture());
        captor.getValue().run();
        verify(pageOpener).accept(WEB_PAGE);
    }
}
