/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import java.util.Optional;

public enum ActionStatus {
    MAIN(false), SECONDARY(true), EXIT_FROM_MAIN(true), EXIT_FROM_SECONDARY(true);

    private final boolean isBusy;

    ActionStatus(final boolean isBusy) {
        this.isBusy = isBusy;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public Optional<ActionStatus> beginSecondary() {
        if (this == MAIN) {
            return Optional.of(SECONDARY);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ActionStatus> beginExit() {
        if (this == MAIN) {
            return Optional.of(EXIT_FROM_MAIN);
        } else if (this == SECONDARY) {
            return Optional.of(EXIT_FROM_SECONDARY);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ActionStatus> complete() {
        if (this == SECONDARY || this == EXIT_FROM_MAIN) {
            return Optional.of(MAIN);
        } else if (this == EXIT_FROM_SECONDARY) {
            return Optional.of(SECONDARY);
        } else {
            return Optional.empty();
        }
    }
}
