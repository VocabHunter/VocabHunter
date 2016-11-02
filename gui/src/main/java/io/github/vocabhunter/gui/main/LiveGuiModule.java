/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import com.google.inject.AbstractModule;
import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.core.GuiTaskHandlerImpl;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.FileListManagerImpl;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.services.*;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;

public class LiveGuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SettingsManager.class).to(SettingsManagerImpl.class);
        bind(FileListManager.class).to(FileListManagerImpl.class);
        bind(FileDialogueFactory.class).to(FileDialogueFactoryImpl.class);
        bind(PlacementManager.class).to(PlacementManagerImpl.class);
        bind(EnvironmentManager.class).to(EnvironmentManagerImpl.class);
        bind(WebPageTool.class).to(WebPageToolImpl.class);
        bind(GuiTaskHandler.class).to(GuiTaskHandlerImpl.class);
    }
}
