package com.example;//dd

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene; // Make the scene variable static

    @Override
 public void start(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        scene = new Scene(root); // Assign the scene to the static variable
        stage.setScene(scene);
        stage.show();
    
    } catch(Exception e) {
        e.printStackTrace();
    }
    } 

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml)); // Now this works because scene is static
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();

    }

}