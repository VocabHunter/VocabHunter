/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.distribution;

import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;

import static io.github.vocabhunter.gui.main.GuiContainerBuilder.createGuiContainer;

public class PackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        VocabHunterGuiExecutable.runApp(args, createGuiContainer(args), a -> launch(a));
    }
}
