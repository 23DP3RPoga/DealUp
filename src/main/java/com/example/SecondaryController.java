package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML private Label userLabel;

    public void setUsername(String username) {
        userLabel.setText("Logged in as: " + username);
    }
}