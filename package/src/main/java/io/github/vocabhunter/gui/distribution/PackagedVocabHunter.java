/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.distribution;

import io.github.vocabhunter.gui.event.CommandLineEventSource;
import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;

import static java.util.Collections.singletonList;

public class PackagedVocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        CommandLineEventSource eventSource = new CommandLineEventSource(args);

        VocabHunterGuiExecutable.runApp(args, singletonList(eventSource), a -> launch(a));
    }
}
