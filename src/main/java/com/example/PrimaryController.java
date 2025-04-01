package com.example;

import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; // Import Label
import javafx.scene.control.Button; // Import Button
import com.opencsv.CSVWriter;




public class PrimaryController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML private TextField nameID;
    @FXML private TextField surnameID;
    @FXML private TextField birthID;
    @FXML private TextField mailID;
    @FXML private TextField userID;
    @FXML private TextField passID;
    @FXML private Label regLabel;
    @FXML private Button confirmID;
    

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("secondary");
   }
    
    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void saveToCSV() {
        String filePath = "C:\\Users\\Reinis\\Documents\\DealUp\\DealUp\\src\\main\\resources\\csv\\register.csv"; // ✅ Saves in project folder

        // Get user input
        String name = nameID.getText();
        String surname = surnameID.getText();
        String age = birthID.getText();
        String mail = mailID.getText();
        String user = userID.getText();
        String pass = passID.getText();

        // Check if fields are empty
        if (name.isEmpty() || surname.isEmpty() || age.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            regLabel.setText("Please fill in all fields!");
            return;
        }

        // Prepare user data
        String[] userData = {name, surname, age, mail, user, pass};

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(userData);
            regLabel.setText("User saved successfully!");
        } catch (IOException e) {
            regLabel.setText("Error saving user.");
            e.printStackTrace();
        }
    }
}


