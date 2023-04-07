package com.example.retea_socializare;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView shieldImageView;

    @FXML
    private Button closeButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private PasswordField setPasswordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label registerMessageLabel;

    @FXML
    private Label usernameMessageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File shieldFile = new File("Images/shield_icon.png");
        Image shieldImage = new Image(shieldFile.toURI().toString());
        shieldImageView.setImage(shieldImage);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public int verifyUsername(String usn) {
        usernameMessageLabel.setText("");
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try (PreparedStatement statement = connectDB.prepareStatement("SELECT * from utilizatori");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                if (username.equals(usn)) {
                    usernameMessageLabel.setText("Username already in use. Choose another one!");
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void registerUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "insert into utilizatori(first_name, last_name, username, password) values('" +
                firstNameTextField.getText() + "','" + lastNameTextField.getText() + "','" +
                usernameTextField.getText() + "','" + setPasswordField.getText() + "')";

        try {
            if(verifyUsername(usernameTextField.getText()) == 0) {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(insertFields);
                registerMessageLabel.setText("User has been registered successfuly!");
                firstNameTextField.clear();
                lastNameTextField.clear();
                usernameTextField.clear();
                setPasswordField.clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
