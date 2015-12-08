/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.distribution;

import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;

import static java.util.Collections.emptyList;

public class PackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        VocabHunterGuiExecutable.runApp(args, emptyList(), a -> launch(a));
    }
}
