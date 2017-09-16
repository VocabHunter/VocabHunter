/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class DelayedExecutorImpl implements DelayedExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(DelayedExecutorImpl.class);

    private final Executor executor;

    private final CountDownLatch latch = new CountDownLatch(1);

    public DelayedExecutorImpl(final Executor executor) {
        this.executor = executor;
    }

    @Override
    public void beginExecution() {
        latch.countDown();
    }

    @Override
    public void execute(final Runnable command) {
        executor.execute(() -> wrapCommand(command));
    }

    private void wrapCommand(final Runnable command) {
        try {
            latch.await();
            command.run();
        } catch (InterruptedException e) {
            LOG.debug("Thread woken unexpectedly", e);
            Thread.currentThread().interrupt();
        }
    }
}
