/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

import io.github.vocabhunter.gui.main.CoreGuiModule;
import io.github.vocabhunter.gui.main.LiveGuiModule;
import io.github.vocabhunter.gui.main.StandardEventSourceModule;
import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;

/**
 * This class is used instead of VocabHunterGuiExecutable to avoid showing the package name in the JavaFX menu.
 * This problem will be solved in a better way in a future version.
 */
public class VocabHunter extends VocabHunterGuiExecutable {
    public static void main(final String... args) {
        runApp(
            args,
            a -> launch(a), // This should remain as a lambda, to keep the short name for the quit menu item
            new CoreGuiModule(), new LiveGuiModule(), new StandardEventSourceModule(args));
    }
}
