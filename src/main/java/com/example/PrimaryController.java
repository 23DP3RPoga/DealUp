package com.example;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;
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
        String filePath = "C:\\Users\\Reinis\\Documents\\DealUp\\DealUp\\src\\main\\resources\\csv\\register.csv"; 

        // Get user input
        String name = nameID.getText();
        String surname = surnameID.getText();
        String ageStr = birthID.getText();
        String mail = mailID.getText();
        String user = userID.getText();
        String pass = passID.getText();

        // Validation: Check if fields are empty
        if (name.isEmpty() || surname.isEmpty() || ageStr.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            regLabel.setText("❌ All fields must be filled!");
            return;
        }

        // Validation: Age must be a number
        if (!ageStr.matches("\\d+")) {
            regLabel.setText("❌ Age must be a number!");
            return;
        }

        
        // Check if username already exists in CSV
        File file = new File(filePath);
        if (file.exists()) {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                List<String[]> records = reader.readAll(); // This can throw CsvException
                for (String[] record : records) {
                    if (record.length > 4 && record[4].equals(user)) { // Username is at index 4
                        regLabel.setText("❌ Username already exists! Choose another.");
                        return;
                    }
                }
            } catch (IOException | com.opencsv.exceptions.CsvException e) {
                regLabel.setText("⚠️ Error reading CSV file.");
                e.printStackTrace();
                return;
            }
        }

        // Prepare user data
        String[] userData = {name, surname, ageStr, mail, user, pass};

        // Ensure directory exists
        file.getParentFile().mkdirs();

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(userData);
            regLabel.setText("✅ User saved successfully!");

            // Clear input fields after saving
            nameID.clear();
            surnameID.clear();
            birthID.clear();
            mailID.clear();
            userID.clear();
            passID.clear();
        } catch (IOException e) {
            regLabel.setText("❌ Error saving user. Please try again.");
            e.printStackTrace();
        } catch (Exception e) {
            regLabel.setText("⚠️ Unexpected error occurred!");
            e.printStackTrace();
        }
    }

    @FXML
    private void readFromCSV() {
        String filePath = "C:\\Users\\Reinis\\Documents\\DealUp\\DealUp\\src\\main\\resources\\csv\\register.csv";

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) { // This can throw CsvValidationException
                System.out.println("Name: " + nextLine[0] + ", Surname: " + nextLine[1]);
            }
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            e.printStackTrace();
        }
    }
}


