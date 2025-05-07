package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {
    @FXML
    private Label user;

    public void setUsername(String username) {
        if (user != null) {
            user.setText(username);
        }
    }
}