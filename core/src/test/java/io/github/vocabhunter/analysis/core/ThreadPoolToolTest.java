/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertNotNull;

public class ThreadPoolToolTest {
    private ExecutorService service;

    @Test
    public void testSingleDaemonExecutor() {
        service = ThreadPoolTool.singleDaemonExecutor("test");
        assertNotNull("Executor service", service);
    }

    @After
    public void tearDown() {
        service.shutdownNow();
    }
}
