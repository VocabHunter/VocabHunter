/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx;

import io.github.vocabhunter.gui.container.GuiContainerBuilder;
import org.picocontainer.MutablePicoContainer;

public final class OsxGuiContainerBuilder {
    private OsxGuiContainerBuilder() {
        // Prevent instantiation - all methods are static
    }

    public static MutablePicoContainer createGuiContainer(final String... args) {
        MutablePicoContainer pico = GuiContainerBuilder.createGuiContainer(args);

        pico.addComponent(OsxEventSource.class);

        return pico;
    }
}
