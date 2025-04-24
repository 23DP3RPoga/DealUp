package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.text.html.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import main.java.com.example.Listing;

public class PrimaryController implements Initializable {
// 
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String[] category = {"Transport", "Real Estate", "Electronics", "Construction", "Clothing", "For home", "For kids", "Production", "Animals", "Hobbies"};

    @FXML private TextField nameID;
    @FXML private TextField surnameID;
    @FXML private TextField birthID;
    @FXML private TextField mailID;
    @FXML private TextField userID;
    @FXML private TextField passID;
    @FXML private Label regLabel;
    @FXML private Button confirmID;
    @FXML private Button chooseImageButton;
    @FXML private ImageView imageView;

    @FXML private ChoiceBox<String> categoryID;
    @FXML private TextField titleListID;
    @FXML private TextArea descListID;
    @FXML private TextField priceListID;
    @FXML private TextField locationListID;
    @FXML private Label listLabel;
    private File lastSelectedImageFile;

    @FXML private TableView<Listing> tableView;
    @FXML private TableColumn<Listing, String> Title;
    @FXML private TableColumn<Listing, String> Description;
    @FXML private TableColumn<Listing, String> Price;
    @FXML private TableColumn<Listing, String> Category;
    @FXML private TableColumn<Listing, String> Location;
    @FXML private TableColumn<Listing, Listing> Image;




    @FXML
    private void switchToSecondary(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("secondary.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    private void saveToCSV(ActionEvent event) {
        String filePath = "src/main/resources/csv/register.csv"; 

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

        // Validation: Email must contain '@' and a '.' after '@'
        if (!mail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            regLabel.setText("❌ Invalid email format!");
            return;
        }

        if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$")) {
            regLabel.setText("❌ Password must have 1 uppercase, 1 lowercase, 1 special character and be at least 6 characters long!");
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

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(pauseEvent -> { // Renamed inner variable to 'pauseEvent'
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("secondary.fxml"));
                    Stage stage = (Stage) regLabel.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    regLabel.setText("⚠️ Error switching scene.");
                    e.printStackTrace();
                }
            });
            pause.play();

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
        String filePath = "src/main/resources/csv/register.csv";

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) { // This can throw CsvValidationException
                System.out.println("Name: " + nextLine[0] + ", Surname: " + nextLine[1]);
            }
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void werifyLogin(ActionEvent event) throws IOException {
        String filePath = "src/main/resources/csv/register.csv";
        String user = userID.getText();
        String pass = passID.getText();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length > 5 && record[4].equals(user) && record[5].equals(pass)) {
                    regLabel.setText("✅ Login successful! Redirecting...");

                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(pauseEvent -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                            Parent root = loader.load();

                            Label label = (Label) root.lookup("#userLabel");
                            if (label != null) {
                                label.setText("Logged in as: " + user);
                            }

                            Stage stage = (Stage) regLabel.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            regLabel.setText("⚠️ Error switching scene.");
                            e.printStackTrace();
                        }
                    });
                    pause.play();

                    return;
                }
            }

            regLabel.setText("❌ Invalid username or password!");

        } catch (IOException | com.opencsv.exceptions.CsvException e) {
            regLabel.setText("⚠️ Error reading CSV file.");
            e.printStackTrace();
        }
    }

    @FXML
    private void signOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToMakeListing(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("makeListing.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Target folder to save the image
                File targetDir = new File("DealUp/src/main/resources/images/listingIMG");
                if (!targetDir.exists()) {
                    targetDir.mkdirs(); // Create the folder if it doesn't exist
                }

                // Create a new file in the target directory with the same name
                File destinationFile = new File(targetDir, selectedFile.getName());

                // Copy the selected file to the destination
                copyFile(selectedFile, destinationFile);

                // Load the copied image and display it
                Image image = new Image(destinationFile.toURI().toString());
                imageView.setImage(image);

                lastSelectedImageFile = destinationFile; // Store the selected image file

                System.out.println("Image saved to: " + destinationFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // File copy utility
    @FXML
    private void copyFile(File source, File destination) throws IOException {
        try (InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
    @FXML
    public void listingCSV() {
        String title = titleListID.getText();
        String description = descListID.getText();
        String price = priceListID.getText();
        String category = categoryID.getValue();
        String location = locationListID.getText();
        String imagePath = (lastSelectedImageFile != null) ? lastSelectedImageFile.getAbsolutePath() : "";

        // Full file path for the CSV
        File csvFile = new File("DealUp/src/main/resources/csv/listing.csv");

        // Make sure parent folders exist
        csvFile.getParentFile().mkdirs();

        String csvLine = String.format("%s,%s,%s,%s,%s,%s\n",
                title, imagePath, description, price, category, location);


        if (title.isEmpty() || description.isEmpty() || price.isEmpty() || category == null || location.isEmpty()) {
            listLabel.setText("❌ All fields must be filled!");
            return;
        }

        if (!Pattern.matches("\\d+(\\.\\d{1,2})?", price)) {
            listLabel.setText("❌ Price must be a number with up to 2 decimal places!");
            return;
        }

        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(csvLine);
            System.out.println("Saved to CSV: " + csvLine);

            titleListID.clear();
            descListID.clear();
            priceListID.clear();
            categoryID.setValue(null);
            locationListID.clear();
            imageView.setImage(null);
            lastSelectedImageFile = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // @Override
    // public void initialize(URL url, ResourceBundle rb) {
    //     if (categoryID == null) {
    //         System.out.println("categoryID is null! Check your FXML file.");
    //     } else {
    //         categoryID.getItems().addAll(category);
    //     }

    //     Title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
    //     Description.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
    //     Price.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrice()));
    //     Category.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
    //     Location.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));

    //     Image.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
    //     Image.setCellFactory(col -> new TableCell<>() {
    //         private final ImageView imageView = new ImageView();

    //         @Override
    //         protected void updateItem(Listing listing, boolean empty) {
    //             super.updateItem(listing, empty);
    //             if (empty || listing == null) {
    //                 setGraphic(null);
    //             } else {
    //                 File file = new File(listing.getImagePath());
    //                 if (file.exists()) {
    //                     Image img = new Image(file.toURI().toString(), 100, 100, true, true);
    //                     imageView.setImage(img);
    //                     imageView.setFitWidth(100);
    //                     imageView.setFitHeight(100);
    //                     setGraphic(imageView);
    //                 } else {
    //                     setGraphic(null);
    //                 }
    //             }
    //         }
    //     });

    //     loadCSVData();
    // }


    // private void loadCSVData() {
    //     File csvFile = new File("src/main/resources/csv/listing.csv");
    //     try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             String[] fields = line.split(",", -1);
    //             if (fields.length >= 6) {
    //                 listings.add(new Listing(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]));
    //             }
    //         }
    //         tableView.setItems(listings);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // public static class Listing {
    //     private final String title, imagePath, description, price, category, location;

    //     public Listing(String title, String imagePath, String description, String price, String category, String location) {
    //         this.title = title;
    //         this.imagePath = imagePath;
    //         this.description = description;
    //         this.price = price;
    //         this.category = category;
    //         this.location = location;
    //     }

    //     public String getTitle() { return title; }
    //     public String getImagePath() { return imagePath; }
    //     public String getDescription() { return description; }
    //     public String getPrice() { return price; }
    //     public String getCategory() { return category; }
    //     public String getLocation() { return location; }
    // }
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ListView<Listing> listingListView;

    private List<Listing> listings = new ArrayList<>();

    @FXML
    private void initialize() {
        // Example listings
        listings.add(new Listing("Cool Shoes", "Used once", 50.0));
        listings.add(new Listing("Vintage Watch", "Looks awesome", 120.0));
        updateListView();
    }

    @FXML
    private void addListing() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());

        Listing newListing = new Listing(title, description, price);
        listings.add(newListing);
        updateListView();

        // Clear input fields
        titleField.clear();
        descriptionField.clear();
        priceField.clear();
    }

    private void updateListView() {
        listingListView.getItems().setAll(listings);
    }
}


