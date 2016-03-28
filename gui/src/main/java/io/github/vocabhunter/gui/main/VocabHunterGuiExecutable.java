/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.common.EnvironmentManager;
import io.github.vocabhunter.gui.controller.GuiFactory;
import io.github.vocabhunter.gui.controller.MainController;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.event.SingleExternalEventSource;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.picocontainer.MutablePicoContainer;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static io.github.vocabhunter.gui.main.ExecutableLogTool.*;
import static io.github.vocabhunter.gui.main.GuiContainerBuilder.createGuiContainer;

public class VocabHunterGuiExecutable extends Application {
    private static final double WINDOW_SIZE_FACTOR = 0.80;

    private static MutablePicoContainer pico;

    public static void setPico(final MutablePicoContainer pico) {
        VocabHunterGuiExecutable.pico = pico;
    }

    @Override
    public void start(final Stage stage) {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> logError(e));
        try {
            EnvironmentManager environmentManager = pico.getComponent(EnvironmentManager.class);
            Dimension screenSize = environmentManager.getScreenSize();
            GuiFactory factory = new GuiFactoryImpl(stage, pico);
            ControllerAndView<MainController, Parent> cav = factory.mainWindow();
            double width = screenSize.getWidth() * WINDOW_SIZE_FACTOR;
            double height = screenSize.getHeight() * WINDOW_SIZE_FACTOR;
            Scene scene = new Scene(cav.getView(), width, height);
            MainController controller = cav.getController();

            scene.setOnKeyPressed(controller.getKeyPressHandler());
            stage.setOnCloseRequest(controller.getCloseRequestHandler());
            stage.setScene(scene);
            stage.show();
        } catch (final RuntimeException e) {
            logError(e);
        }
    }

    public static void main(final String... args) {
        runApp(args, createGuiContainer(args), a -> launch(a));
    }

    protected static void runApp(final String[] args, final MutablePicoContainer pico, final Consumer<String[]> launcher) {
        logStartup();
        try {
            logSystemDetails();
            setPico(pico);
            eventSources(pico).forEach(s -> s.setListener(pico.getComponent(ExternalEventBroker.class)));
            launcher.accept(args);
        } finally {
            logShutdown();
        }
    }

    private static List<SingleExternalEventSource> eventSources(final MutablePicoContainer pico) {
        return pico.getComponents(SingleExternalEventSource.class);
    }
}
