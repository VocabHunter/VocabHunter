/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.gui.common.GuiTaskHandler;
import io.github.vocabhunter.gui.common.GuiTaskHandlerImpl;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.services.*;
import io.github.vocabhunter.gui.settings.SettingsManager;

public class LiveGuiModule extends AbstractModule {
    private final BootstrapContext context;

    public LiveGuiModule(final BootstrapContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        bind(FileDialogueFactory.class).to(FileDialogueFactoryImpl.class);
        bind(PlacementManager.class).to(PlacementManagerImpl.class);
        bind(EnvironmentManager.class).to(EnvironmentManagerImpl.class);
        bind(WebPageTool.class).to(WebPageToolImpl.class);
        bind(GuiTaskHandler.class).to(GuiTaskHandlerImpl.class);
    }

    @Provides
    @Singleton
    public SettingsManager provideSettingsManager() {
        return context.getSettingsManager();
    }

    @Provides
    @Singleton
    public FileListManager provideFileListManager() {
        return context.getFileListManager();
    }
}
