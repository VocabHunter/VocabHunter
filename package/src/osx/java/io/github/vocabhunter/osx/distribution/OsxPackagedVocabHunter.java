/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx.distribution;

import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;
import io.github.vocabhunter.osx.OsxEventSource;

import static java.util.Collections.singletonList;

public class OsxPackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        OsxEventSource eventSource = new OsxEventSource();

        VocabHunterGuiExecutable.runApp(args, singletonList(eventSource), a -> launch(a));
    }
}
