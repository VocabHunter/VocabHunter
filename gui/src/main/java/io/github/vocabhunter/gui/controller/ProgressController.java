/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.ProgressModel;
import jakarta.inject.Inject;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

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

    private final I18nManager i18nManager;

    @Inject
    public ProgressController(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void initialise(final ProgressModel model) {
        buildChartResults(model);
        buildChartProgress(model);
    }

    private void buildChartProgress(final ProgressModel model) {
        Data done = slice(PROGRESS_SLICE_MARKED, model.markedProperty());
        Data remaining = slice(PROGRESS_SLICE_UNMARKED, model.unseenUnfilteredProperty());

        chartProgress.setData(FXCollections.observableArrayList(
            done, remaining
        ));

        bindValueLabel(labelValueDone, model.markedProperty());
        bindPercentLabel(labelPercentDone, model.markedPercentVisibleProperty());
        bindValueLabel(labelValueRemaining, model.unseenUnfilteredProperty());
        bindPercentLabel(labelPercentRemaining, model.unseenUnfilteredPercentVisibleProperty());
    }

    private void buildChartResults(final ProgressModel model) {
        Data known = slice(PROGRESS_SLICE_KNOWN, model.knownProperty());
        Data unknown = slice(PROGRESS_SLICE_UNKNOWN, model.unknownProperty());
        Data unmarked = slice(PROGRESS_SLICE_UNMARKED, model.unseenUnfilteredProperty());
        Data filtered = slice(PROGRESS_SLICE_FILTERED, model.unseenFilteredProperty());

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

    private Data slice(final I18nKey key, final ObservableNumberValue property) {
        double value = property.getValue().intValue();
        Data slice = new Data(i18nManager.text(key), value);

        slice.pieValueProperty().bind(property);

        return slice;
    }

    private void bindValueLabel(final Label valueLabel, final ObservableNumberValue property) {
        StringExpression binding = i18nManager.textBinding(PROGRESS_WORD_COUNT, property);

        valueLabel.textProperty().bind(binding);
    }

    private void bindPercentLabel(final Label valueLabel, final ObservableNumberValue property) {
        valueLabel.textProperty().bind(i18nManager.textBinding(PROGRESS_WORD_PERCENTAGE, property));
    }
}
