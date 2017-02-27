/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import com.google.inject.AbstractModule;
import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.analysis.core.ThreadPoolToolImpl;
import io.github.vocabhunter.analysis.grid.*;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.session.SessionWordsToolImpl;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import io.github.vocabhunter.gui.services.FilterFileModelTranslator;
import io.github.vocabhunter.gui.services.FilterFileModelTranslatorImpl;
import io.github.vocabhunter.gui.status.StatusManager;
import io.github.vocabhunter.gui.status.StatusManagerImpl;

public class CoreGuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ThreadPoolTool.class).to(ThreadPoolToolImpl.class);
        bind(Analyser.class).to(SimpleAnalyser.class);

        bind(DocumentGridReader.class).to(DocumentGridReaderImpl.class);
        bind(ExcelGridReader.class).to(ExcelGridReaderImpl.class);
        bind(SessionWordsTool.class).to(SessionWordsToolImpl.class);
        bind(GridWordsExtractor.class).to(GridWordsExtractorImpl.class);
        bind(TextGridBuilder.class).to(TextGridBuilderImpl.class);
        bind(TextGridManager.class).to(TextGridManagerImpl.class);
        bind(FilterFileWordsExtractor.class).to(FilterFileWordsExtractorImpl.class);

        bind(StatusManager.class).to(StatusManagerImpl.class);
        bind(FilterFileModelTranslator.class).to(FilterFileModelTranslatorImpl.class);
    }
}
