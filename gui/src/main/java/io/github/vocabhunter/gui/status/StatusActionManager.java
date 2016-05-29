/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface StatusActionManager {
    void wrapHandler(Supplier<Boolean> handler, Consumer<StatusManager> beginStatus);
}
