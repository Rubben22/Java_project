package com.example.retea_socializare;

import database.DatabaseConnection;
import domain.Friend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class RequestController implements Initializable {
    @FXML
    private TableColumn<Friend, String> first_name;

    @FXML
    private TableColumn<Friend, String> last_name;

    @FXML
    private TableView<Friend> suggestionTable;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField searchTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            first_name.setCellValueFactory(new PropertyValueFactory<Friend, String>("firstname"));
            last_name.setCellValueFactory(new PropertyValueFactory<>("lastname"));

            ObservableList<Friend> list = load_names();
            suggestionTable.setItems(list);

            FilteredList<Friend> filteredUsers = new FilteredList<>(list, b -> true);

            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredUsers.setPredicate(Friend -> {

                    if(newValue.isEmpty() || newValue.isBlank()){
                        return true;
                    }

                    String searchKeyboard = newValue.toLowerCase();

                    if(Friend.getFirstname().toLowerCase().contains(searchKeyboard)){
                        return true;
                    }else return Friend.getLastname().toLowerCase().contains(searchKeyboard);
                });
            });

            SortedList<Friend> sortedData = new SortedList<>(filteredUsers);

            sortedData.comparatorProperty().bind(suggestionTable.comparatorProperty());

            suggestionTable.setItems(sortedData);


        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Friend> load_names() {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            ObservableList<Friend> data = FXCollections.observableArrayList();
            ResultSet rs = connectDB.createStatement().executeQuery("select * from utilizatori");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            LoginController friendsController = loader.getController();
            String user_looged = friendsController.get_user_logged();

            while (rs.next()) {
                String username_baza = rs.getString("username");
                String last_name_utilizator = rs.getString("last_name");
                String first_name_utilizator = rs.getString("first_name");

                if (!user_looged.equals(username_baza)) {
                    ResultSet rs2 = connectDB.createStatement().executeQuery("select * from friends");
                    int x = 0;

                    while (rs2.next()) {
                        String username1b = rs2.getString("username1");
                        String username2b = rs2.getString("username2");

                        if ((Objects.equals(username_baza, username1b) && user_looged.equals(username2b)) ||
                                (Objects.equals(username_baza, username2b) && user_looged.equals(username1b)))
                            x = 1;
                    }
                    ResultSet rs3 = connectDB.createStatement().executeQuery("select * from requests");
                    while (rs3.next()) {
                        String username1 = rs3.getString("username1");
                        String username2 = rs3.getString("username2");
                        if ((username1.equals(user_looged) && username2.equals(username_baza))) {
                            x = 1;
                        }
                    }
                    if (x == 0) {
                        Friend suggestion = new Friend(user_looged, username_baza, first_name_utilizator, last_name_utilizator);
                        data.add(suggestion);
                    }
                }
            }
            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    void sendFriendRequest(ActionEvent event) throws IOException {
        int selectedID = suggestionTable.getSelectionModel().getSelectedIndex();
        Friend fr = suggestionTable.getItems().get(selectedID);
        //suggestionTable.getItems().remove(selectedID);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController friendsController = loader.getController();
        String user_looged = friendsController.get_user_logged();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sql = "insert into requests(username1, username2, first_name, last_name) values (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connectDB.prepareStatement(sql);
            ps.setString(1, fr.getUsername1());
            ps.setString(2, fr.getUsername2());
            ResultSet rs = connectDB.createStatement().executeQuery("select * from utilizatori");
            while (rs.next()) {
                String username_baza = rs.getString("username");
                String last_name_utilizator = rs.getString("last_name");
                String first_name_utilizator = rs.getString("first_name");
                if (user_looged.equals(username_baza)) {
                    ps.setString(3, oglinda(first_name_utilizator));
                    ps.setString(4, oglinda(last_name_utilizator));
                }
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String oglinda(String string) {
        return string;
    }

}
