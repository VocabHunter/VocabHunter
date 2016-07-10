/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SessionViewTool {
    private final TabPane tabPane = new TabPane();

    private final Map<SessionTab, Tab> tabMap = new EnumMap<>(SessionTab.class);

    private final Map<Tab, SessionTab> reverseMap = new HashMap<>();

    private final SimpleObjectProperty<SessionTab> selected = new SimpleObjectProperty<>();

    public SessionViewTool() {
        ObservableList<Tab> tabs = tabPane.getTabs();

        for (SessionTab tabDescription : SessionTab.values()) {
            Tab tab = new Tab(tabDescription.getName());

            tab.setId(tabDescription.getId());
            tab.setClosable(false);
            tabs.add(tab);
            tabMap.put(tabDescription, tab);
            reverseMap.put(tab, tabDescription);
        }
        selected.bind(Bindings.createObjectBinding(this::getSelectedTab, tabPane.getSelectionModel().selectedItemProperty()));
    }

    private SessionTab getSelectedTab() {
        return reverseMap.get(tabPane.getSelectionModel().getSelectedItem());
    }

    public Node getView() {
        return tabPane;
    }

    public SimpleObjectProperty<SessionTab> selectedProperty() {
        return selected;
    }

    public void setTabContent(final SessionTab tabDescription, final Node content) {
        tabMap.get(tabDescription).setContent(content);
    }
}
