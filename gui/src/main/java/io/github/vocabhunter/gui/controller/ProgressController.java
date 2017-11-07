/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.model.ProgressModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;

import java.text.MessageFormat;

public class ProgressController {
    @FXML
    private PieChart chartProgress;

    @FXML
    private Label labelValueDone;

    @FXML
    private Label labelValueRemaining;

    @FXML
    private PieChart chartResults;

    @FXML
    private Label labelValueKnown;

    @FXML
    private Label labelValueUnknown;

    @FXML
    private Label labelValueUnseenUnfiltered;

    @FXML
    private Label labelValueFiltered;

    @FXML
    private Label labelPercentDone;

    @FXML
    private Label labelPercentRemaining;

    @FXML
    private Label labelPercentKnown;

    @FXML
    private Label labelPercentUnknown;

    @FXML
    private Label labelPercentUnseenUnfiltered;

    @FXML
    private Label labelPercentFiltered;

    public void initialise(final ProgressModel model) {
        buildChartResults(model);
        buildChartProgress(model);
    }

    private void buildChartProgress(final ProgressModel model) {
        Data done = slice("Marked as\nKnown/Unknown", model.markedProperty());
        Data remaining = slice("Unmarked", model.unseenUnfilteredProperty());

        chartProgress.setData(FXCollections.observableArrayList(
            done, remaining
        ));

        bindValueLabel(labelValueDone, model.markedProperty());
        bindPercentLabel(labelPercentDone, model.markedPercentVisibleProperty());
        bindValueLabel(labelValueRemaining, model.unseenUnfilteredProperty());
        bindPercentLabel(labelPercentRemaining, model.unseenUnfilteredPercentVisibleProperty());
    }

    private void buildChartResults(final ProgressModel model) {
        Data known = slice("Marked as\nKnown", model.knownProperty());
        Data unknown = slice("Marked as\nUnknown", model.unknownProperty());
        Data unmarked = slice("Unmarked", model.unseenUnfilteredProperty());
        Data filtered = slice("Filtered", model.unseenFilteredProperty());

        chartResults.setData(FXCollections.observableArrayList(
            known, unknown, unmarked, filtered
        ));

        bindValueLabel(labelValueKnown, model.knownProperty());
        bindPercentLabel(labelPercentKnown, model.knownPercentProperty());
        bindValueLabel(labelValueUnknown, model.unknownProperty());
        bindPercentLabel(labelPercentUnknown, model.unknownPercentProperty());
        bindValueLabel(labelValueUnseenUnfiltered, model.unseenUnfilteredProperty());
        bindPercentLabel(labelPercentUnseenUnfiltered, model.unseenUnfilteredPercentProperty());
        bindValueLabel(labelValueFiltered, model.unseenFilteredProperty());
        bindPercentLabel(labelPercentFiltered, model.unseenFilteredPercentProperty());
    }

    private Data slice(final String name, final ObservableNumberValue property) {
        double value = property.getValue().intValue();
        Data slice = new Data(name, value);

        slice.pieValueProperty().bind(property);

        return slice;
    }

    private void bindValueLabel(final Label valueLabel, final ObservableNumberValue property) {
        StringBinding binding = Bindings.createStringBinding(() -> formatWords(property), property);

        valueLabel.textProperty().bind(binding);
    }

    private String formatWords(final ObservableNumberValue property) {
        return MessageFormat.format("{0} {0,choice,0#Words|1#Word|1<Words}", property.intValue());
    }

    private void bindPercentLabel(final Label valueLabel, final ObservableNumberValue property) {
        valueLabel.textProperty().bind(Bindings.format("%.0f%%", property));
    }
}
