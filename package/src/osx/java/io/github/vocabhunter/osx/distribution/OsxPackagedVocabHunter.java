/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx.distribution;

import io.github.vocabhunter.gui.event.CommandLineEventSource;
import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;
import io.github.vocabhunter.osx.OsxEventSource;

import static java.util.Arrays.asList;

public class OsxPackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        OsxEventSource osxEventSource = new OsxEventSource();
        CommandLineEventSource commandLineEventSource = new CommandLineEventSource(args);

        VocabHunterGuiExecutable.runApp(args, asList(commandLineEventSource, osxEventSource), a -> launch(a));
    }
}
