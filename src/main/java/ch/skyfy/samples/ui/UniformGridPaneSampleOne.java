package ch.skyfy.samples.ui;

import ch.skyfy.samples.ui.utils.FXMLUtils;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is an example of a responsive GUI.
 * The goal here is to create a square grid (with the same number of columns and rows) that adapts to the size of the window.
 * This grid is called uniformGridPane.
 *
 * Here, we are using a StackPane as root that will contain our UniformGridPane.
 *
 * To make our uniformGridPane responsive, we set the MaxSize and MinSize properties to USE_PREF_SIZE
 * And we set its PrefSize property when the window is resized
 */
public class UniformGridPaneSampleOne extends StackPane implements Initializable {

    public UniformGridPaneSampleOne() {
        FXMLUtils.loadFXML("ui/fxml/UniformGridPaneSampleOne.fxml", this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        buildUniformGridPane(3);
//        buildUniformGridPane(4);
        buildUniformGridPane(9);
//        buildUniformGridPane(18);
    }

    /**
     * @param numberColAndRow the number of col and row
     */
    @SuppressWarnings("SameParameterValue")
    private void buildUniformGridPane(int numberColAndRow) {
        var uniformGridPane = new GridPane();
        uniformGridPane.setGridLinesVisible(true);
        uniformGridPane.setId("uniformGridPane");
        uniformGridPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        uniformGridPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

        // create 3 cols and 3 rows
        // Our uniformGridPane will contain 9 cells in total.
        var sizePercent = 100d / 3d;
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
                cell.setPadding(new Insets(10));
                cell.setBackground(new Background(new BackgroundFill(Color.rgb(40 + colored, 10 + (colored / 2), (int) (120d + (colored / 3)), 0.9), new CornerRadii(20), cell.getPadding())));
                uniformGridPane.add(cell, i, j);
                colored += colored >= 115 ? -colored + 10 : 15;
            }
        }

        // Update size of the uniformGridPane when the Windows is resized
        this.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getHeight() == 0 || newValue.getWidth() == 0) return;

            var margin = StackPane.getMargin(uniformGridPane);

            var width = newValue.getWidth() - margin.getLeft() - margin.getRight();
            var height = newValue.getHeight() - margin.getTop() - margin.getBottom();

            var size = Math.min(width, height);
            uniformGridPane.setPrefSize(size, size);
        });

        StackPane.setMargin(uniformGridPane, new Insets(100));
        this.getChildren().add(uniformGridPane);
    }

}
