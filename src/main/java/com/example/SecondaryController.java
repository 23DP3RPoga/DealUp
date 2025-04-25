package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {

    @FXML private Label userLabel;

    public void setUserLabel(String username) {
        userLabel.setText("Logged in as: " + username);
    }

}
