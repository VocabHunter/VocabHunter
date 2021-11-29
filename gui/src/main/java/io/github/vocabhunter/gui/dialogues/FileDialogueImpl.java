/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FileDialogueImpl implements FileDialogue {
    private final I18nManager i18nManager;

    private final FileDialogueType type;

    private final Window window;

    private final SettingsManager settingsManager;

    private final List<ExtensionFilter> filters;

    private final BiFunction<FileChooser, Window, File> openFunction;

    private final Function<SettingsManager, Path> pathGetter;

    private final BiConsumer<SettingsManager, Path> pathSetter;

    private FileChoice selected;

    public FileDialogueImpl(final I18nManager i18nManager, final FileDialogueType type, final Window window, final SettingsManager settingsManager,
                            final List<FileFormatType> formats, final BiFunction<FileChooser, Window, File> openFunction, final Function<SettingsManager, Path> pathGetter,
                            final BiConsumer<SettingsManager, Path> pathSetter) {
        this.i18nManager = i18nManager;
        this.window = window;
        this.type = type;
        this.settingsManager = settingsManager;
        this.filters = formats.stream()
            .map(f -> f.buildFilter(i18nManager))
            .toList();
        this.openFunction = openFunction;
        this.pathGetter = pathGetter;
        this.pathSetter = pathSetter;
    }

    @Override
    public void showChooser() {
        FileChooser chooser = new FileChooser();

        chooser.setTitle(i18nManager.text(type.getTitleKey()));
        chooser.getExtensionFilters().addAll(filters);
        File initialDirectory = pathGetter.apply(this.settingsManager).toFile();
        chooser.setInitialDirectory(initialDirectory);

        selected = showChooser(chooser);
    }

    private FileChoice showChooser(final FileChooser chooser) {
        File result = openFunction.apply(chooser, window);

        if (result == null) {
            return null;
        } else {
            Path file = result.toPath();
            ExtensionFilter extensionFilter = chooser.getSelectedExtensionFilter();
            FileFormatType chosenType = FileFormatType.getByFilter(extensionFilter);

            pathSetter.accept(this.settingsManager, file.getParent());

            return new FileChoice(file, chosenType);
        }
    }

    @Override
    public boolean isFileSelected() {
        return selected != null;
    }

    @Override
    public Path getSelectedFile() {
        return selected.file();
    }

    @Override
    public FileFormatType getFileFormatType() {
        return selected.type();
    }
}
