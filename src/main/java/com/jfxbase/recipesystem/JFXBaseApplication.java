package com.jfxbase.recipesystem;

import com.jfxbase.recipesystem.utils.Environment;
import com.jfxbase.recipesystem.utils.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//get SceneBuilder
public class JFXBaseApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("log-in-view.fxml")));

        stage.setTitle(Environment.APP_TITLE);
        stage.setFullScreen(Environment.IS_FULLSCREEN);
        stage.setScene(new Scene(root, 600, 600));
        stage.show();

        Logger.info("Application started..");
    }

    public static void main(String[] args) {
        launch();


    }
}