/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.gui.model.ProgressModel;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class ProgressController {
    public PieChart chartResults;

    public PieChart chartProgress;

    public void initialise(final ProgressModel model) {
        buildChartResults(model);
        buildChartProgress(model);
    }

    private void buildChartResults(final ProgressModel model) {

        Data known = slice("Marked as\nKnown", model.knownProperty());
        Data unknown = slice("Marked as\nUnknown", model.unknownProperty());
        Data unmarked = slice("Unmarked", model.unseenUnfilteredProperty());
        Data filtered = slice("Filtered", model.unseenFilteredProperty());

        ObservableList<Data> slices = FXCollections.observableArrayList(
            known, unknown, unmarked, filtered
        );

        chartResults.setData(slices);
    }

    private void buildChartProgress(final ProgressModel model) {
        NumberBinding doneValue = model.knownProperty().add(model.unknownProperty());

        Data done = slice("Marked as\nKnown/Unknown", doneValue);
        Data remaining = slice("Unmarked", model.unseenUnfilteredProperty());

        ObservableList<Data> slices = FXCollections.observableArrayList(
            done, remaining
        );

        chartProgress.setData(slices);
    }

    private Data slice(final String name, final ObservableValue<Number> property) {
        double value = property.getValue().doubleValue();
        Data slice = new Data(name, value);

        slice.pieValueProperty().bind(property);

        return slice;
    }
}
