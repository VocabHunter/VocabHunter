/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.i18n.I18nManager;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

@Singleton
public class FxmlHandler {
    private final Provider<FXMLLoader> loaderProvider;

    private final I18nManager i18nManager;

    @Inject
    public FxmlHandler(final Provider<FXMLLoader> loaderProvider, final I18nManager i18nManager) {
        this.loaderProvider = loaderProvider;
        this.i18nManager = i18nManager;
    }

    public <C, V extends Node> ControllerAndView<C, V> loadControllerAndView(final ViewFxml fxml) {
        FXMLLoader loader = loaderProvider.get();

        V view = loadNode(loader, fxml);
        C controller = loader.getController();

        return new ControllerAndView<>(controller, view);
    }

    public <T extends Node> T loadNode(final ViewFxml fxml) {
        return loadNode(loaderProvider.get(), fxml);
    }

    private <T> T loadNode(final FXMLLoader loader, final ViewFxml fxml) {
        String name = fxml.getName();

        try {

            loader.setLocation(getClass().getResource("/" + name));
            loader.setResources(i18nManager.bundle());

            return loader.load();
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load FXML '%s'", name), e);
        }
    }
}
