/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.analysis.core.ThreadPoolToolImpl;
import io.github.vocabhunter.analysis.file.TextReader;
import io.github.vocabhunter.analysis.file.TikaTool;
import io.github.vocabhunter.analysis.grid.*;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.session.SessionWordsToolImpl;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import io.github.vocabhunter.gui.services.ExternalEventBroker;
import io.github.vocabhunter.gui.services.ExternalEventBrokerImpl;
import io.github.vocabhunter.gui.services.FilterFileModelTranslator;
import io.github.vocabhunter.gui.services.FilterFileModelTranslatorImpl;
import io.github.vocabhunter.gui.status.StatusManager;
import io.github.vocabhunter.gui.status.StatusManagerImpl;

import java.nio.file.Paths;
import java.util.List;
import javax.inject.Singleton;

public class CoreGuiModule extends AbstractModule {
    private final List<String> args;

    public CoreGuiModule(final String... args) {
        this.args = List.of(args);
    }

    @Override
    protected void configure() {
        bind(ThreadPoolTool.class).to(ThreadPoolToolImpl.class);
        bind(TextReader.class).to(TikaTool.class);
        bind(Analyser.class).to(SimpleAnalyser.class);

        bind(DocumentGridReader.class).to(DocumentGridReaderImpl.class);
        bind(ExcelGridReader.class).to(ExcelGridReaderImpl.class);
        bind(SessionWordsTool.class).to(SessionWordsToolImpl.class);
        bind(GridWordsExtractor.class).to(GridWordsExtractorImpl.class);
        bind(TextGridBuilder.class).to(TextGridBuilderImpl.class);
        bind(TextGridManager.class).to(TextGridManagerImpl.class);
        bind(FilterFileWordsExtractor.class).to(FilterFileWordsExtractorImpl.class);
        bind(I18nManager.class).to(I18nManagerImpl.class);

        bind(StatusManager.class).to(StatusManagerImpl.class);
        bind(FilterFileModelTranslator.class).to(FilterFileModelTranslatorImpl.class);
    }

    @Provides
    @Singleton
    public ExternalEventBroker provideExternalEventBroker() {
        ExternalEventBroker broker = new ExternalEventBrokerImpl();

        if (!args.isEmpty()) {
            broker.openFile(Paths.get(args.get(0)));
        }

        return broker;
    }
}
