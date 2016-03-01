/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx.distribution;

import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;

import static io.github.vocabhunter.osx.OsxGuiContainerBuilder.createGuiContainer;

public class OsxPackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        VocabHunterGuiExecutable.runApp(args, createGuiContainer(args), a -> launch(a));
    }
}
