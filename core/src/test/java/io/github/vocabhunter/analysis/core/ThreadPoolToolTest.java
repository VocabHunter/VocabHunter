/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ThreadPoolToolTest {
    private static final int EXPECTED_RESULT = 123;

    private ExecutorService service;

    @Test
    public void testSingleDaemonExecutor() {
        service = ThreadPoolTool.singleDaemonExecutor("test");
        assertNotNull("Executor service", service);
    }

    @Test
    public void testDaemonThreadRun() throws Exception {
        service = ThreadPoolTool.singleDaemonExecutor("test");

        Future<Integer> future = service.submit(() -> EXPECTED_RESULT);
        int result = future.get();

        assertEquals("Result", EXPECTED_RESULT, result);
    }

    @After
    public void tearDown() {
        service.shutdownNow();
    }
}
