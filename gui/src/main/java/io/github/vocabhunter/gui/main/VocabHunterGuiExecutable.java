/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.common.ToolkitManager;
import io.github.vocabhunter.gui.controller.MainController;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.event.SingleExternalEventSource;
import io.github.vocabhunter.gui.factory.ControllerAndView;
import io.github.vocabhunter.gui.factory.GuiFactory;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.picocontainer.MutablePicoContainer;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static io.github.vocabhunter.gui.container.GuiContainerBuilder.createGuiContainer;
import static io.github.vocabhunter.gui.main.ExecutableLogTool.*;

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
            ToolkitManager toolkitManager = pico.getComponent(ToolkitManager.class);
            Dimension screenSize = toolkitManager.getScreenSize();
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

    public static void runApp(final String[] args, final MutablePicoContainer pico, final Consumer<String[]> launcher) {
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
