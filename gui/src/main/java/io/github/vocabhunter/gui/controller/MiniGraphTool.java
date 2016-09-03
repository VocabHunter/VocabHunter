/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.model.StatusModel;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;

public final class MiniGraphTool {
    private static final String STYLE_CLASS = "miniGraph";

    private static final int WIDTH = 75;

    private MiniGraphTool() {
        // Prevent instantiation - all methods are static
    }

    public static ProgressBar miniGraph(final StatusModel statusModel) {
        ProgressBar bar = new ProgressBar();

        bar.getStyleClass().add(STYLE_CLASS);
        bar.managedProperty().bind(statusModel.graphShownProperty());
        bar.visibleProperty().bind(statusModel.graphShownProperty());
        bar.progressProperty().bind(statusModel.markedFractionProperty());
        bar.setPrefWidth(WIDTH);

        Tooltip tooltip = new Tooltip();
        bar.setTooltip(tooltip);
        tooltip.textProperty().bind(statusModel.graphTextProperty());

        return bar;
    }
}
