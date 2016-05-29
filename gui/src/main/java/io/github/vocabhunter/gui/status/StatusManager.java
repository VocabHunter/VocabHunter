/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.StatusModel;

public interface StatusManager {
    void initialise(StatusModel model);

    void beginNewSession();

    void beginOpenSession();

    void beginSaveSession();

    void beginExport();

    void performAction();

    void markSuccess();

    void completeAction();
}
