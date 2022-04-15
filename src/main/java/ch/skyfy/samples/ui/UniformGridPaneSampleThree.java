package ch.skyfy.samples.ui;

import ch.skyfy.samples.ui.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.*;

/**
 * This is an example of a responsive GUI.
 * The goal here is to create a square grid (with the same number of columns and rows) that adapts to the size of the window.
 * This grid is called the uniformGridPane.
 * <p>
 * The basic layout where all our elements will be placed is a StackPane
 * <p>
 * We want the uniformGridPane to be centered. We also want it to have margins based on a percentage.
 * For this, our uniformGridPane is placed in a traditional GridPane (called root_GridPane) where we specified percentage in rows and columns
 * <p>
 * To make our uniformGridPane responsive, we set the MaxSize and MinSize properties to USE_PREF_SIZE
 * And we set its PrefSize property when the window is resized
 * <p>
 * In this Third example, we have some extra feature like responsive padding
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class UniformGridPaneSampleThree extends StackPane implements Initializable {

    @FXML
    private GridPane root_GridPane;

    private final Map<Double, Double> cellWidthPadding;
    private final Map<Double, Double> cellHeightPadding;

    public UniformGridPaneSampleThree() {
        cellWidthPadding = new HashMap<>() {{
            put(50d, 1d); // If the width is between 50(exclusive) and 100 (inclusive) a padding of 1 will be applied
            put(100d, 5d); // If the width is between 100(exclusive) and 200 (inclusive) a padding of 5 will be applied
            put(200d, 10d);
            put(300d, 15d);
            put(850d, 50d);
        }};
        cellHeightPadding = new HashMap<>() {{
            put(50d, 1d); // If the width is between 50(exclusive) and 100 (inclusive) a padding of 1 will be applied
            put(100d, 5d); // If the width is between 100(exclusive) and 200 (inclusive) a padding of 5 will be applied
            put(200d, 10d);
            put(300d, 15d);
            put(850d, 50d);
        }};

        FXMLUtils.loadFXML("ui/fxml/UniformGridPaneSampleThree.fxml", this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        buildUniformGridPane(3);
        buildUniformGridPane(4);
//        buildUniformGridPane(9);
//        buildUniformGridPane(18);
    }

    /**
     * @param numberColAndRow the number of col and row
     */
    @SuppressWarnings("SameParameterValue")
    private void buildUniformGridPane(int numberColAndRow) {
        var uniformGridPane = new GridPane();
        uniformGridPane.setId("uniformGridPane");
        uniformGridPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        uniformGridPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

        // create 3 cols and 3 rows
        // Our uniformGridPane will contain 9 cells in total.
        var sizePercent = 100d / numberColAndRow;
        for (byte i = 0; i < numberColAndRow; i++) {
            var row = new RowConstraints() {{
                setPercentHeight(sizePercent);
            }};
            var col = new ColumnConstraints() {{
                setPercentWidth(sizePercent);
            }};
            uniformGridPane.getRowConstraints().add(row);
            uniformGridPane.getColumnConstraints().add(col);
        }

        // Adding cell to our uniformGridPane
        var colored = 10;
        for (byte i = 0; i < numberColAndRow; i++) {
            for (byte j = 0; j < numberColAndRow; j++) {
                var cell = new StackPane();
                cell.setPadding(new Insets(50));
                cell.setBackground(new Background(new BackgroundFill(Color.rgb(40 + colored, 10 + (colored / 2), (int) (120d + (colored / 3)), 0.9), new CornerRadii(50), cell.getPadding())));
                uniformGridPane.add(cell, i, j);
                colored += colored >= 135 ? -colored + 10 : 10;
            }
        }

        // Make padding responsive
        uniformGridPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getHeight() == 0 || newValue.getWidth() == 0) return;

            Double correctWidth = null, correctHeight = null;
            var listWidth = new ArrayList<>(cellWidthPadding.keySet());
            Collections.sort(listWidth);
            for (var key : listWidth) {
                if (newValue.getWidth() > key) correctWidth = cellWidthPadding.get(key);
                else if (newValue.getWidth() <= key) break;
            }

            var listHeight = new ArrayList<>(cellHeightPadding.keySet());
            Collections.sort(listHeight);
            for (var key : listHeight) {
                if (newValue.getHeight() > key) correctHeight = cellHeightPadding.get(key);
                else if (newValue.getHeight() <= key) break;
            }

            if (correctWidth == null || correctHeight == null) return;

            for (var child : uniformGridPane.getChildren()) {
                if (child instanceof StackPane stackPane) {
                    var oldBC = stackPane.getBackground().getFills().get(0);
                    stackPane.setPadding(new Insets(correctHeight, correctWidth, correctHeight, correctWidth));
                    stackPane.setBackground(new Background(new BackgroundFill(oldBC.getFill(), oldBC.getRadii(), stackPane.getPadding())));
                }
            }
        });

        // Update size of the uniformGridPane when the Windows is resized
        root_GridPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getWidth() == 0 || newValue.getHeight() == 0) return;

            // Getting rowConstraint and columnConstraint where our uniformGridPane is placed
            var indexes = getRowAndColConstraints(uniformGridPane, root_GridPane);
            if (indexes == null) return;

            var columnConstraints = indexes.getKey();
            var rowConstraints = indexes.getValue();

            // We calculate the new size of the cell in which our uniformGridPane is placed
            var cellWidth = newValue.getWidth() * (columnConstraints.getPercentWidth() / 100d);
            var cellHeight = newValue.getHeight() * (rowConstraints.getPercentHeight() / 100d);

            // To make our uniformGridPane square, we take the smallest size and apply it as width and height
            var size = Math.min(cellWidth, cellHeight);
            uniformGridPane.setPrefSize(size, size);
        });

        root_GridPane.add(uniformGridPane, 1, 1);
    }

    private @Nullable Pair<ColumnConstraints, RowConstraints> getRowAndColConstraints(Node node, GridPane gridPane) {
        for (byte i = 0; i < gridPane.getColumnConstraints().size(); i++)
            for (byte j = 0; j < gridPane.getRowConstraints().size(); j++)
                for (Node ignored : gridPane.getChildren())
                    if (GridPane.getRowIndex(node) == j && GridPane.getColumnIndex(node) == i) {
                        return new Pair<>(gridPane.getColumnConstraints().get(i), gridPane.getRowConstraints().get(j));
                    }
        return null;
    }

}
