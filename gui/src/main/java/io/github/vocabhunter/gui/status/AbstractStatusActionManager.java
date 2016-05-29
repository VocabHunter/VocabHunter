/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractStatusActionManager implements StatusActionManager {
    private final StatusManager statusManager;

    protected AbstractStatusActionManager(final StatusManager statusManager) {
        this.statusManager = statusManager;
    }

    @Override
    public void wrapHandler(final Supplier<Boolean> handler, final Consumer<StatusManager> beginStatus) {
        beginStatus.accept(statusManager);

        executeHandler(() -> runHandler(handler));
    }

    protected abstract void executeHandler(Runnable handler);

    private void runHandler(final Supplier<Boolean> handler) {
        try {
            boolean isSuccessfulAction = handler.get();

            if (isSuccessfulAction) {
                statusManager.markSuccess();
            }
        } finally {
            statusManager.completeAction();
        }
    }
}
