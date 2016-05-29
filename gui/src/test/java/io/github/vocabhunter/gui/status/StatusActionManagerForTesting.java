/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

public class StatusActionManagerForTesting extends AbstractStatusActionManager {
    public StatusActionManagerForTesting(final StatusManager statusManager) {
        super(statusManager);
    }

    @Override
    protected void executeHandler(final Runnable handler) {
        handler.run();
    }
}
