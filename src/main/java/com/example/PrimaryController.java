package com.example;


import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import javafx.scene.control.TableCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.concurrent.Task;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class PrimaryController implements Initializable {
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String[] category = {"Transport", "Real Estate", "Electronics", "Construction", "Clothing", "For home", "For kids", "Production", "Animals", "Hobbies"};

    @FXML private TableView<Listing> tableView;
    @FXML private TableColumn<Listing, String> titleCol;
    @FXML private TableColumn<Listing, String> imageCol;
    @FXML private TableColumn<Listing, String> descCol;
    @FXML private TableColumn<Listing, String> priceCol;
    @FXML private TableColumn<Listing, String> categoryCol;
    @FXML private TableColumn<Listing, String> dateCol;
    @FXML private TableColumn<Listing, String> locationCol;

    @FXML private TableView<Listing> myListings;
     
    
    @FXML
    private TableColumn<Listing, String> titleCol2, descCol2, priceCol2, categoryCol2, dateCol2, locationCol2, imageCol2;
    
    @FXML
    private TableColumn<Listing, Void> optionsCol;
    

    
    @FXML private TextField nameID;
    @FXML private TextField surnameID;
    @FXML private DatePicker birthID;
    @FXML private TextField mailID;
    @FXML private TextField userID;
    @FXML private TextField passID;
    @FXML private Label regLabel;
    
    
    @FXML private Button confirmID;
    @FXML private Button chooseImageButton;
    @FXML private ImageView imageView;

    @FXML private MenuButton Catagories;
    @FXML private MenuItem Houses;
    @FXML private MenuItem sortByDate;
    @FXML private MenuItem sortByAge;
    @FXML private ChoiceBox<String> categoryID;
    @FXML private TextField titleListID;
    @FXML private TextArea descListID;
    @FXML private TextField priceListID;
    @FXML private Label fee;
    @FXML private TextField locationListID;
    @FXML private Label listLabel;
    @FXML private Label user;
    private File lastSelectedImageFile;
    private LocalDateTime dateID;   
    

    @FXML
    private void switchToSecondary(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvException {
        String username  = user.getText();
        root = FXMLLoader.load(getClass().getResource("LoggedIn.fxml"));
        Label userLabel = (Label) root.lookup("#user");
        if (userLabel != null) {
            userLabel.setText(username);

        // No csv nolasas vardu un uzvardu, ja ir tad ieliek labelā
        String filePath = "src/main/resources/csv/register.csv";
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
            if (record.length > 4 && record[4].equals(username)) { 
                String name = record[0];
                String surname = record[1];
                Label label = (Label) root.lookup("#userLabel");
                if (label != null) {
                label.setText(name + " " + surname);
                }
                break;
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }}
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
    private void saveToCSV(ActionEvent event) throws com.opencsv.exceptions.CsvException {
        String filePath = "src/main/resources/csv/register.csv"; 
        String name = nameID.getText();
        String surname = surnameID.getText();
        LocalDate birthDate = birthID.getValue(); 
        String mail = mailID.getText();
        String user = userID.getText();
        String pass = passID.getText();
    
        // Validation: Check if fields are empty
        if (name.isEmpty() || surname.isEmpty() || birthDate == null || mail.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            regLabel.setText("❌ All fields must be filled!");
            return;
        }
        
        // Age validation
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        if (age < 16) {
            regLabel.setText("❌ You must be at least 16 years old to register!");
            return;
        }
    
        // Convert date to string to save it in CSV
        String dateStr = birthDate.toString();

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
            } catch (IOException e) {
                regLabel.setText("⚠️ Error reading CSV file.");
                e.printStackTrace();
                return;
            }
        }

        // Prepare user data
        String[] userData = {name, surname, dateStr, mail, user, pass};

        // Ensure directory exists
        file.getParentFile().mkdirs();

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(userData);
            regLabel.setText("✅ User saved successfully!");

            // Clear input fields after saving
            nameID.clear();
            surnameID.clear();
            birthID.setValue(null);
            mailID.clear();
            userID.clear();
            passID.clear();

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(pauseEvent -> { // Renamed inner variable to 'pauseEvent'
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
                    Parent root = loader.load();

                    Label userLabel = (Label) root.lookup("#user");
                            if (userLabel != null) {
                                userLabel.setText(user);
                            }
                    Label label = (Label) root.lookup("#userLabel");
                    if (label != null) {
                        label.setText(name+" "+surname);
                    }
                    Stage stage = (Stage) regLabel.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    }
                 catch (IOException e) {
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


                    String name = record[0];
                    String surname =record[1];

                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(pauseEvent -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
                            Parent root = loader.load();
                            // Pass the username to the next scene using a label
                            Label userLabel = (Label) root.lookup("#user");
                            if (userLabel != null) {
                                userLabel.setText(user);
                            }
                            Label label = (Label) root.lookup("#userLabel");
                            if (label != null) {
                                label.setText(name+" "+surname);
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
        String username  = user.getText();
        root = FXMLLoader.load(getClass().getResource("makeListing.fxml"));
        Label userLabel = (Label) root.lookup("#user");
        if (userLabel != null) {
            userLabel.setText(username);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        }
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
                File targetDir = new File("src/main/resources/images/listingIMG");
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

                System.out.println("Image saved to: " + destinationFile.getPath());    

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
        String title = titleListID.getText().trim();
        String description = descListID.getText().trim();
        String price = priceListID.getText().trim();
        String category = categoryID.getValue() != null ? categoryID.getValue().trim() : "";
        String location = locationListID.getText().trim();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateID = date.format(myFormatObj);
        String imagePath = (lastSelectedImageFile != null) ? "images/listingIMG/" + lastSelectedImageFile.getName(): "";
        String username = user.getText();
        // Validate if fields are empty
        if (title.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty() || location.isEmpty()) {
            listLabel.setText("❌ All fields must be filled!");
            return;
        }
        if (imagePath.isEmpty()){
            imagePath = "";
        }
        // Validate price format
        if (!Pattern.matches("\\d+(\\.\\d{1,2})?", price)) {
            listLabel.setText("❌ Price must be a number with up to 2 decimal places!");
            return;
        }

        // Full file path for the CSV
        File csvFile = new File("src/main/resources/csv/listing.csv");

        // Make sure parent folders exist
        csvFile.getParentFile().mkdirs();

        // Prepare data for CSV
        String[] listingData = {imagePath, title, description, price, category, dateID, location,username};

        // Write to CSV file
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true))) {
            writer.writeNext(listingData);  // Use CSVWriter to properly format and handle special characters
            System.out.println("Saved to CSV: " + Arrays.toString(listingData));

            // Clear input fields after saving
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


    public void readFromCSV() {
        String filePath = "src/main/resources/csv/listing.csv";
    
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Skip empty lines or lines that don't have exactly 7 fields
                if (nextLine.length != 7 || isRowEmpty(nextLine)) {
                    System.out.println("Skipping line: " + Arrays.toString(nextLine));  // Debugging line
                    continue;  // Skip this line if it doesn't have the required 7 fields or is empty
                }
                // Print the values from the CSV for debugging
                System.out.println("Title: " + nextLine[1] + ", Description: " + nextLine[2]);
            }
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            e.printStackTrace();
        }
    }
    


    private boolean isRowEmpty(String[] row) {
    for (String field : row) {
        if (field != null && !field.trim().isEmpty()) {
            return false;
        }
    }
    return true;
    }
    private boolean isUserListingsLoaded = false;

    @FXML
    private void loadListings() {
        Task<ObservableList<Listing>> task = new Task<>() {
            @Override
            protected ObservableList<Listing> call() throws Exception {
                ObservableList<Listing> data = FXCollections.observableArrayList();
                File csvFile = new File("src\\main\\resources\\csv\\listing.csv");

                if (!csvFile.exists()) {
                    System.out.println("Listing CSV not found.");
                    return data;
                }

                try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                    String[] fields;
                    reader.readNext(); // Skip the header line if there is one
                    while ((fields = reader.readNext()) != null) {
                        if (fields.length >= 7) {
                            String title = fields[1].trim();
                            String image = fields[0].trim();
                            String description = fields[2].trim();
                            String price = fields[3].trim();
                            String category = fields[4].trim();
                            String date = fields[5].trim();
                            String location = fields[6].trim();

                            data.add(new Listing(image, title, description, price, category, date, location));
                        }
                    }
                }
                return data;
            }
        };

        task.setOnSucceeded(event -> tableView.setItems(task.getValue()));
        task.setOnFailed(event -> task.getException().printStackTrace());

        new Thread(task).start();
    }





    @FXML
    private TextField searchBar;
    
    private final ListingSearchService searchService = new ListingSearchService();
    
    @FXML
    private void handleSearch(ActionEvent event) {
        try {
            String keyword = searchBar.getText().toLowerCase();
            ObservableList<Listing> results = searchService.search(keyword);
            tableView.setItems(results);
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            e.printStackTrace();
        }
    }
    @FXML TextField guestSearchBar;

    @FXML
    private void guestSearchListings(ActionEvent event) {
        try {
            // Get the keyword/input from guestSearchBar
            String keyword = guestSearchBar.getText().toLowerCase();

            // Switch to guest.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guest.fxml"));
            Parent root = loader.load();

            // Pass the keyword to the search bar in the new scene
            TextField searchBarInGuest = (TextField) root.lookup("#searchBar");
            if (searchBarInGuest != null) {
                searchBarInGuest.setText(keyword);

                // Simulate pressing Enter to trigger the search
                searchBarInGuest.fireEvent(new ActionEvent());
            }

            // Display the new scene
            Stage stage = (Stage) guestSearchBar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private MenuButton sortByCategories;

    @FXML
    private void handleMenuSelection(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        sortByCategories.setText(selectedItem.getText());
        String keyword2 = selectedItem.getText().toLowerCase();
        if (keyword2.equals("reset")) {
            sortByCategories.setText("Categories");
            loadListings(); // Reset the table to show all listings
        } else { 
            try {
                ObservableList<Listing> results = searchService.searchByCatagories(keyword2);
                tableView.setItems(results);
            } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }
           
        

    @FXML Button search;

    @FXML
    private void searchWithBoth(ActionEvent event) {
        try {
            // Get the selected category from the MenuButton
            String keyword2 = sortByCategories.getText().toLowerCase();

            // Get the input from the TextField
            String keyword = searchBar.getText().toLowerCase();

            // Ensure the category is not "Categories" (default/reset state)
            if (keyword2.equals("categories")) {
                keyword2 = ""; // Treat it as no category filter
            }

            // Perform the search using both keywords
            ObservableList<Listing> results = searchService.searchByBoth(keyword, keyword2);

            // Update the TableView with the search results
            tableView.setItems(results);
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void switchToProfile(ActionEvent event) throws IOException {
        String username  = user.getText();
        root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Label user = (Label) root.lookup("#user");
        if (user != null) {
            user.setText(username);
        
            // Read from CSV 
            String filePath = "src/main/resources/csv/register.csv";
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                List<String[]> records = reader.readAll();
                for (String[] record : records) {
                    if (record.length > 5 && record[4].equals(username)) { // Username is at index 4
                        String name = record[0]; // Name is at index 0
                        String surname = record[1]; // Surname is at index 1
                        String mail = record[3]; // Email is at index 3
                        String pass = record[5]; // Password is at index 5

                        // Lookup and set the values in the corresponding fields
                        TextField nameID = (TextField) root.lookup("#nameID");
                        TextField surnameID = (TextField) root.lookup("#surnameID");
                        TextField mailID = (TextField) root.lookup("#mailID");
                        PasswordField passID = (PasswordField) root.lookup("#passID");

                        if (nameID != null) nameID.setText(name);
                        if (surnameID != null) surnameID.setText(surname);
                        if (mailID != null) mailID.setText(mail);
                        if (passID != null) passID.setText(pass);

                        Label label = (Label) root.lookup("#userLabel");
                        if (label != null) {
                            label.setText(name + " " + surname);
                        }
                        break;
                    }
                }
            } catch (IOException | com.opencsv.exceptions.CsvException e) {
                e.printStackTrace();
            }
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML Label userLabel;

    @FXML
    private void changeName(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvValidationException, com.opencsv.exceptions.CsvException {
        String newValue = nameID.getText();
        String username = user.getText(); // Get the current username
        String filePath = "src/main/resources/csv/register.csv";
        if (newValue.isEmpty()) {
            regLabel.setText("❌ Name field cannot be empty!");
            return;
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
            if (record.length > 4 && record[4].equals(username)) { 
                record[0] = newValue;
                String surname= record[1];
                userLabel.setText(newValue+' '+surname);
                break;
            }
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(records);
            regLabel.setText("✅ Name updated successfully!");
            }
        }   catch (IOException | com.opencsv.exceptions.CsvException e) {
            regLabel.setText("⚠️ Error updating name. Please try again."); 
            e.printStackTrace();
        }
        }

    @FXML
    private void changeSurname(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvException {
        String newValue = surnameID.getText();
        String username = user.getText(); // Get the current username
        String filePath = "src/main/resources/csv/register.csv";
        if (newValue.isEmpty()) {
            regLabel.setText("❌ Surname field cannot be empty!");
            return;
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            // Find the record with the matching username
            for (String[] record : records) {
            if (record.length > 4 && record[4].equals(username)) { // Username is at index 4
                record[1] = newValue; // Update the name field (index 0)
                String name= record[0];
                userLabel.setText(name+' '+newValue);
                break;
            }
            }
            // Write the updated records back to the CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                writer.writeAll(records);
                regLabel.setText("✅ Surname updated successfully!");
            } // Properly close the try block


        } catch (IOException | com.opencsv.exceptions.CsvException e) {
            e.printStackTrace();  
            regLabel.setText("⚠️ Error updating surname. Please try again.");  
            }
        }   

    @FXML
    private void changeEmail(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvException {
        String newValue = mailID.getText().trim();
        String username = user.getText(); // Get the current username
        String filePath = "src/main/resources/csv/register.csv";

        // Validate email format
        if (newValue.isEmpty()) {
            regLabel.setText("❌ Email field cannot be empty!");
            return;
        }
        if (!newValue.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            regLabel.setText("❌ Invalid email format!");
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            // Find the record with the matching username
            for (String[] record : records) {
            if (record.length > 4 && record[4].equals(username)) { // Username is at index 4
                record[3] = newValue; // Update the email field (index 3)
                break;
            }
            }

            // Write the updated records back to the CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(records);
            regLabel.setText("✅ Email updated successfully!");
            }
        } catch (IOException | com.opencsv.exceptions.CsvException e) {
            regLabel.setText("⚠️ Error updating email. Please try again.");
            e.printStackTrace();
        }}
       

    
 
    
    
    @FXML
    private void changePass(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvException {

        String newValue = passID.getText();
        String username = user.getText(); // Get the current username
        String filePath = "src/main/resources/csv/register.csv";

        if (newValue.isEmpty()) {
            regLabel.setText("❌ Password field cannot be empty!");
            return;
        }
        if (!newValue.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$")) {
            regLabel.setText("❌ Password must have 1 uppercase, 1 lowercase, 1 special character and be at least 6 characters long!");
            return;
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            // Find the record with the matching username
            for (String[] record : records) {
            if (record.length > 4 && record[4].equals(username)) { // Username is at index 4
                record[5] = newValue; // Update the name field (index 0)
                break;
            }
            }

            // Write the updated records back to the CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(records);
            regLabel.setText("✅ Password updated successfully!");
            }
        } catch (IOException | com.opencsv.exceptions.CsvException e) {
            regLabel.setText("⚠️ Error updating password. Please try again.");
            e.printStackTrace();
        }
    }   
  

   
    @FXML
    private void switchToMyListings(ActionEvent event) throws IOException, com.opencsv.exceptions.CsvException {
        String username = user.getText();
        root = FXMLLoader.load(getClass().getResource("userListings.fxml"));

        Label userLabel = (Label) root.lookup("#user");
             if (userLabel != null) { 
                userLabel.setText(username);
              }
        // Read from register.csv to get name + surname
        String filePath = "src/main/resources/csv/register.csv";
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length > 4 && record[4].equals(username)) {
                    String name = record[0];
                    String surname = record[1];
                    
                    Label nameLabel = (Label) root.lookup("#userLabel");
                    if (nameLabel != null) {
                        nameLabel.setText(name + " " + surname);
                    }
                    
                    break;
                }
            }
        }
    
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void loadUserListings() {
        Task<ObservableList<Listing>> task = new Task<>() {
            @Override
            protected ObservableList<Listing> call() throws Exception {
                String username = "Admin1";
                System.out.println(username+"Test");
                ObservableList<Listing> data = FXCollections.observableArrayList();
                File csvFile = new File("src\\main\\resources\\csv\\listing.csv");
    
                if (!csvFile.exists()) {
                    System.out.println("Listing CSV not found.");
                    return data;
                }
                try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                    String[] fields;
                    

                    reader.readNext(); // Skip header
                    while ((fields = reader.readNext()) != null) {

                        if (fields[7].equals(username)) {

                            String title = fields[1].trim();
                            String image = fields[0].trim();
                            String description = fields[2].trim();
                            String price = fields[3].trim();
                            String category = fields[4].trim();
                            String date = fields[5].trim();
                            String location = fields[6].trim();

                            data.add(new Listing(image, title, description, price, category, date, location));
                        }

                       
                        }
                    }
                return data;
            }
        };
    
        task.setOnSucceeded(event -> myListings.setItems(task.getValue()));
        task.setOnFailed(event -> task.getException().printStackTrace());
    
        new Thread(task).start();
    }








    @Override
    public void initialize(URL url, ResourceBundle rb) {

    
    
        if (myListings != null) {
            // Set up table columns
            titleCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
            descCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
            priceCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrice()));
            categoryCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
            dateCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));
            locationCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));
    
            // Set up image column
            imageCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getImage()));
            imageCol2.setCellFactory(col -> new TableCell<>() {
                private final ImageView imageView = new ImageView();
    
                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    if (empty || imagePath == null || imagePath.isEmpty()) {
                        setGraphic(null);
                    } else {
                        File imageFile = new File("src/main/resources/" + imagePath);
                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            setGraphic(imageView);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            });
    
            // Set up options column
            optionsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));
            optionsCol.setCellFactory(col -> new TableCell<>() {

                private final Button deleteButton = new Button("Delete");
                private final HBox buttonBox = new HBox(5,  deleteButton);
    
                {

                    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
    
                   
    
                    deleteButton.setOnAction(event -> {
                        Listing listing = getTableView().getItems().get(getIndex());
                        System.out.println("Deleting: " + listing.getTitle());
                        deleteListing(listing);
                    });
                      
                }
    
                @Override
              protected void updateItem(Void item, boolean empty) {
                  super.updateItem(item, empty);
                  setGraphic(empty ? null : buttonBox);
              }

               

            });
                loadUserListings(); 

        }
    
    
            if (tableView != null) {
    
                // Set up other columns
                titleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
                descCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
                priceCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrice()));
                categoryCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
                dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));
                locationCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));
                // Set up the image column to display images
                imageCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getImage()));
                imageCol.setCellFactory(col -> new TableCell<>() {
                    private final ImageView imageView = new ImageView();
    
                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);
                        if (empty || imagePath == null || imagePath.isEmpty()) {
                            setGraphic(null);
                        } else {
                            File imageFile = new File("src/main/resources/" + imagePath);
                            if (imageFile.exists()) {
                                Image image = new Image(imageFile.toURI().toString());
                                imageView.setImage(image);
    
                                // Configure the ImageView to maintain aspect ratio
    
                                imageView.setPreserveRatio(true);
                                imageView.setFitWidth(100); // Set the desired width
                                imageView.setFitHeight(100); // Set the desired height
    
                                setGraphic(imageView);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });
    
    
                
                loadListings();
            } 
    
            if (priceListID != null && fee != null) {
                priceListID.textProperty().addListener((obs, oldText, newText) -> {
                    try {
                        double price = Double.parseDouble(newText.trim());
                        
                        double feeAmount;
            
                        // Apply different fee rates based on the price
                        if (price < 10) {
                            feeAmount = 2.0; // Fee is 2 EUR if price is below 10 EUR
                        } else if (price <= 500) {
                            feeAmount = price * 0.03; // 3% fee if price is between 10 and 500 EUR
                        } else if (price <= 2000) {
                            feeAmount = price * 0.02; // 2% fee if price is between 500 and 2000 EUR
                        } else {
                            feeAmount = price * 0.018; // 1.4% fee if price is above 2000 EUR
                        }
            
                        fee.setText(String.format("Fee: €%.2f", feeAmount)); // Update the fee label
                    } catch (NumberFormatException e) {
                        fee.setText("Invalid price"); // Handle invalid input
                    }
                });
            }
        }
    
        



















        private void openEditDialog(Listing listing) {
        // Open a dialog to edit the listing details
        // For now, we'll just update the title of the listing as an example

        TextInputDialog dialog = new TextInputDialog(listing.getTitle());
        dialog.setTitle("Edit Listing");
        dialog.setHeaderText("Edit the title of the listing:");
        dialog.setContentText("Title:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newTitle -> {
            // Update the title of the listing in the table view
            listing.setTitle(newTitle);
            // After editing, update the CSV file
            updateListingInFile(listing);
        });
    }

    // Delete the listing from the table and CSV file
    private void deleteListing(Listing listing) {
        // Remove the listing from the table view
        myListings.getItems().remove(listing);

        // Now, update the CSV file by removing the corresponding record
        deleteListingFromFile(listing);
    }

    // Update the modified listing in the CSV file
    private void updateListingInFile(Listing updatedListing) {
        File csvFile = new File("src/main/resources/csv/listing.csv");

        if (!csvFile.exists()) {
            System.out.println("Listing CSV not found.");
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();
            List<String[]> updatedRecords = new ArrayList<>();

            for (String[] record : records) {
                if (record.length >= 8 && record[1].equals(updatedListing.getTitle())) {
                    // Update the record with new values
                    record[0] = updatedListing.getImage(); // Update image path
                    record[1] = updatedListing.getTitle(); // Update title
                    record[2] = updatedListing.getDescription(); // Update description
                    record[3] = updatedListing.getPrice(); // Update price
                    record[4] = updatedListing.getCategory(); // Update category
                    // Update date
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    record[5] = now.format(formatter); // Update date with the current local time
                    record[6] = updatedListing.getLocation(); // Update location
                }
                updatedRecords.add(record);
            }

            // Write the updated records back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
                writer.writeAll(updatedRecords);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // Delete the listing from the CSV file
    private void deleteListingFromFile(Listing listing) {
        File csvFile = new File("src/main/resources/csv/listing.csv");

        if (!csvFile.exists()) {
            System.out.println("Listing CSV not found.");
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();
            List<String[]> updatedRecords = new ArrayList<>();

            for (String[] record : records) {
                if (!(record.length >= 8 && record[1].equals(listing.getTitle()))) {
                    updatedRecords.add(record); // Keep records that don't match the listing to delete
                }
            }

            // Write the updated records back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
                writer.writeAll(updatedRecords);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
