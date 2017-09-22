/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

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

public class GuiTaskHandlerTest {
    @Mock
    private ThreadPoolTool threadPoolTool;

    @Mock
    private ExecutorService executorService;

    @Mock
    private Consumer<Runnable> guiThreadRunner;

    @Mock
    private Runnable task;

    @Captor
    private ArgumentCaptor<Runnable> captor;

    private GuiTaskHandler target;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        when(threadPoolTool.singleDaemonExecutor(anyString())).thenReturn(executorService);
        target = new GuiTaskHandlerImpl(threadPoolTool, guiThreadRunner);
    }

    @Test
    public void testExecuteInBackGround() {
        target.executeInBackground(task);
        verify(executorService).execute(task);
    }

    @Test
    public void testExecuteOnGuiThread() {
        target.executeOnGuiThread(task);
        verify(guiThreadRunner).accept(task);
    }

    @Test
    public void testPauseThenExecuteOnGuiThread() {
        target.pauseThenExecuteOnGuiThread(task);
        verify(executorService).execute(captor.capture());
        captor.getValue().run();
        verify(guiThreadRunner).accept(task);
    }
}
