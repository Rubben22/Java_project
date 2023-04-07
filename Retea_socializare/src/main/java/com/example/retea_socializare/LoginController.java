package com.example.retea_socializare;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button cancelButton;

    @FXML
    private ImageView lockImageView;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField enterPasswordField;

    public static String user_logged = "";

    public String get_user_logged(){
        return user_logged;
    }

    public void set_user_logged(String username){
        user_logged = username;
    }

    @FXML
    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("Images/background.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("Images/lock.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "Select count(1) from utilizatori where username ='" + usernameTextField.getText() + "' and password='" + enterPasswordField.getText() + "'";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    set_user_logged(usernameTextField.getText());
                    loginAccountForm();
                }else
                    loginMessageLabel.setText("Invalid login. Try again ");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void createAccountForm(){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
            Stage registerStage = new Stage();
            registerStage.setTitle("Create account");
            registerStage.setScene(new Scene(root, 600,435));
            registerStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loginAccountForm(){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("friends.fxml")));
            Stage registerStage = new Stage();
            registerStage.setTitle("Logged In");
            registerStage.setScene(new Scene(root, 715,427));
            registerStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
