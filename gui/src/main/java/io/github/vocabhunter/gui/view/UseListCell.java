/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.common.WordUseTool;
import io.github.vocabhunter.gui.model.WordModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class UseListCell extends TextFieldListCell<String> {

    private final SimpleObjectProperty<WordModel> currentWord;

    public UseListCell(final SimpleObjectProperty<WordModel> currentWord) {
        this.currentWord = currentWord;
    }

    @Override
    public void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            String word = currentWord.get().getIdentifier();

            setGraphic(flow(word, item));
            setText(null);
        }
    }

    private TextFlow flow(final String word, final String use) {
        Text[] texts = new WordUseTool(word, use)
                .stream()
                .map(s -> text(word, s))
                .toArray(Text[]::new);

        return new TextFlow(texts);
    }

    private Text text(final String word, final String s) {
        Text text = new Text(s);

        if (s.equalsIgnoreCase(word)) {
            text.getStyleClass().add("wordUse");
        }
        return text;
    }
}
