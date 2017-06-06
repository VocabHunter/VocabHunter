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

    private static final int THREAD_COUNT = 4;

    private final ThreadPoolTool target = new ThreadPoolToolImpl();

    private ExecutorService service;

    @Test
    public void testSingleDaemonExecutor() {
        service = target.singleDaemonExecutor("test");
        assertNotNull("Executor service", service);
    }

    @Test
    public void testMultipleDaemonExecutor() {
        service = target.daemonExecutor("test", THREAD_COUNT);
        assertNotNull("Executor service", service);
    }

    @Test
    public void testSingleDaemonThreadRun() throws Exception {
        service = target.singleDaemonExecutor("test");

        validateExecution();
    }

    @Test
    public void testMultipleDaemonThreadRun() throws Exception {
        service = target.daemonExecutor("test", THREAD_COUNT);

        validateExecution();
    }

    private void validateExecution() throws Exception {
        Future<Integer> future = service.submit(() -> EXPECTED_RESULT);
        int result = future.get();

        assertEquals("Result", EXPECTED_RESULT, result);
    }

    @After
    public void tearDown() {
        service.shutdownNow();
    }
}
