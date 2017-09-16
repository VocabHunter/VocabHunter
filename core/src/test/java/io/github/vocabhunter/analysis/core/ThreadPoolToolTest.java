/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ThreadPoolToolTest {
    private static final int EXPECTED_RESULT = 123;

    private static final int THREAD_COUNT = 4;

    private final ThreadPoolTool target = new ThreadPoolToolImpl();

    @Test
    public void testSingleDaemonExecutor() {
        Executor service = target.singleDaemonExecutor("test");

        assertNotNull("Executor service", service);
    }

    @Test
    public void testMultipleDaemonExecutor() {
        Executor service = target.delayedExecutor("test", THREAD_COUNT);

        assertNotNull("Executor service", service);
    }

    @Test
    public void testSingleDaemonThreadRun() throws Exception {
        Executor service = target.singleDaemonExecutor("test");

        validateExecution(service);
    }

    @Test
    public void testMultipleDaemonThreadRun() throws Exception {
        DelayedExecutor service = target.delayedExecutor("test", THREAD_COUNT);

        service.beginExecution();

        validateExecution(service);
    }

    private void validateExecution(final Executor service) throws Exception {
        Future<Integer> future = CompletableFuture.supplyAsync(() -> EXPECTED_RESULT, service);
        int result = future.get();

        assertEquals("Result", EXPECTED_RESULT, result);
    }

    @After
    public void tearDown() {
        target.forceShutdown();
    }
}
