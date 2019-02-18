/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadPoolToolTest {
    private static final int EXPECTED_RESULT = 123;

    private final ThreadPoolTool target = new ThreadPoolToolImpl();

    @Test
    public void testGuiThreadPool() throws Exception {
        Executor service = target.guiThreadPool();

        validateExecution(service);
    }

    @Test
    public void testFilterThreadPool() throws Exception {
        DelayedExecutor service = target.filterThreadPool();

        service.beginExecution();

        validateExecution(service);
    }

    private void validateExecution(final Executor service) throws Exception {
        Future<Integer> future = CompletableFuture.supplyAsync(() -> EXPECTED_RESULT, service);
        int result = future.get();

        assertEquals(EXPECTED_RESULT, result, "Result");
    }

    @AfterEach
    public void tearDown() {
        target.forceShutdown();
    }
}
