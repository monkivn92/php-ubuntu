package com.vuongpv.swan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SwanMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( SwanMain.class.getResource("/com/vuongpv/swan/swan.fxml"));

        Pane mainPane = fxmlLoader.load();

        final SwanController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.getIcons().add(new Image(SwanMain.class.getResourceAsStream("Book-icon.png")));
        primaryStage.setTitle("SWAN BOOK");
        primaryStage.setScene(new Scene(mainPane, 1000, 640));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
