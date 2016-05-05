/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SessionViewTool {
    private final Tab analysisTab = new Tab("Text Analysis");

    private final Tab progressTab = new Tab("Progress Report");

    private final TabPane tabPane = new TabPane(analysisTab, progressTab);

    public SessionViewTool() {
        analysisTab.setClosable(false);
        progressTab.setClosable(false);
    }

    public void addAnalysisView(final Node view) {
        analysisTab.setContent(view);
    }

    public void addProgressView(final Node view) {
        progressTab.setContent(view);
    }

    public Node getView() {
        return tabPane;
    }
}
