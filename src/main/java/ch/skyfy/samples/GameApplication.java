package ch.skyfy.samples;

import ch.skyfy.samples.ui.UniformGridPaneSampleThree;
import ch.skyfy.samples.ui.UniformGridPaneSampleTwo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(800);
        primaryStage.setHeight(480);

//        primaryStage.setScene(new Scene(new UniformGridPaneSampleOne()));
//        primaryStage.setScene(new Scene(new UniformGridPaneSampleTwo()));
        primaryStage.setScene(new Scene(new UniformGridPaneSampleThree()));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
