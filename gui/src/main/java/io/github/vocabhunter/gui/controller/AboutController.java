/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.BuildInfo;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.common.WebPageTool;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController {
    public Button buttonClose;

    public Hyperlink linkWebsite;

    public Label labelVersion;

    public void initialise(final Stage stage) {
        buttonClose.setOnAction(e -> stage.close());
        linkWebsite.setOnAction(e -> WebPageTool.showWebPage(GuiConstants.WEBSITE));
        setupVersion();
    }

    private void setupVersion() {
        String template = labelVersion.getText();
        String text = String.format(template, BuildInfo.version());

        labelVersion.setText(text);
    }
}
