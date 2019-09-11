/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.BuildInfo;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.services.WebPageTool;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.inject.Inject;

public class AboutController {
    private final I18nManager i18nManager;

    private final WebPageTool webPageTool;

    @FXML
    private Button buttonClose;

    @FXML
    private Hyperlink linkWebsite;

    @FXML
    private Hyperlink linkTwitter;

    @FXML
    private Hyperlink linkGithub;

    @FXML
    private Hyperlink linkGithubStar;

    @FXML
    private Label labelVersion;

    @Inject
    public AboutController(final I18nManager i18nManager, final WebPageTool webPageTool) {
        this.i18nManager = i18nManager;
        this.webPageTool = webPageTool;
    }

    public void initialise(final Stage stage) {
        buttonClose.setOnAction(e -> stage.close());
        linkWebsite.setOnAction(e -> webPageTool.showWebPage(I18nKey.LINK_MAIN));
        linkTwitter.setOnAction(e -> webPageTool.showWebPage(GuiConstants.TWITTER));
        linkGithub.setOnAction(e -> webPageTool.showWebPage(GuiConstants.GITHUB));
        linkGithubStar.setOnAction(e -> webPageTool.showWebPage(GuiConstants.GITHUB));

        labelVersion.setText(i18nManager.text(I18nKey.ABOUT_VERSION, BuildInfo.version()));
    }
}
