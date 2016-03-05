/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.container;

import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import io.github.vocabhunter.gui.common.ToolkitManagerImpl;
import io.github.vocabhunter.gui.event.CommandLineEventSource;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.main.FileDialogueFactoryImpl;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

public final class GuiContainerBuilder {
    private GuiContainerBuilder() {
        // Prevent instantiation - all methods are static
    }

    public static MutablePicoContainer createBaseContainer(final String... args) {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching());

        pico.addComponent(SimpleAnalyser.class);
        pico.addComponent(FileStreamer.class);

        pico.addComponent(new CommandLineEventSource(args));
        pico.addComponent(ExternalEventBroker.class);

        return pico;
    }

    public static MutablePicoContainer createGuiContainer(final String... args) {
        MutablePicoContainer pico = createBaseContainer(args);

        pico.addComponent(SettingsManagerImpl.class);
        pico.addComponent(FileDialogueFactoryImpl.class);
        pico.addComponent(ToolkitManagerImpl.class);

        return pico;
    }
}
