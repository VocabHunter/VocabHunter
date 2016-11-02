/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import com.google.inject.AbstractModule;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import io.github.vocabhunter.gui.status.StatusManager;
import io.github.vocabhunter.gui.status.StatusManagerImpl;

public class CoreGuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Analyser.class).to(SimpleAnalyser.class);
        bind(StatusManager.class).to(StatusManagerImpl.class);
    }
}
