package com.example.retea_socializare;

import database.DatabaseConnection;
import domain.Friend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;


public class FriendsController implements Initializable {
    @FXML
    private TableColumn<Friend, String> firstname;

    @FXML
    private TableColumn<Friend, String> lastname;

    @FXML
    private TableColumn<Friend, String> username2;

    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Friend> table;

    @FXML
    private TableColumn<Friend, String> first_name_request;

    @FXML
    private TableColumn<Friend, String> last_name_request;

    @FXML
    private TableView<Friend> request_table;

    @FXML
    private TableColumn<Friend, String> action;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username2.setCellValueFactory(new PropertyValueFactory<Friend, String>("username2"));
        firstname.setCellValueFactory(new PropertyValueFactory<Friend, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<Friend, String>("lastname"));

        ObservableList<Friend> list2 = load_names();
        table.setItems(list2);
        first_name_request.setCellValueFactory(new PropertyValueFactory<Friend, String>("firstname"));
        last_name_request.setCellValueFactory(new PropertyValueFactory<Friend, String>("lastname"));

        ObservableList<Friend> list = load_requests();
        request_table.setItems(list);
        Callback<TableColumn<Friend, String>, TableCell<Friend, String>> cellFactory = (param) ->{
            final TableCell<Friend, String> cell = new TableCell<Friend, String>() {

                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty){
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button acceptButton = new Button("Accept");
                        final Button deletebutton = new Button("Reject");
                        acceptButton.setOnAction(event -> {
                            Friend f = getTableView().getItems().get(getIndex());
                            new_friendship(f);
                            request_table.getItems().remove(f);
                            delete_request(f);
                        });
                        deletebutton.setOnAction(event -> {
                            Friend friend = getTableView().getItems().get(getIndex());
                            delete_request(friend);
                            request_table.getItems().remove(friend);
                        });
                        HBox managebtn = new HBox(acceptButton, deletebutton);
                        managebtn.setStyle("-fx-alignment: center");
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        action.setCellFactory(cellFactory);

        request_table.setItems(list);
    }

    @FXML
    void logoutAction(ActionEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    public void new_friendship(Friend friendship){
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String sql = "insert into friends(username1, first_name, last_name, username2) values (?, ?, ?, ?)";
            PreparedStatement statement = connectDB.prepareStatement(sql);
            statement.setString(1, friendship.getUsername1());
            statement.setString(2, friendship.getFirstname());
            statement.setString(3, friendship.getLastname());
            statement.setString(4, friendship.getUsername2());

            statement.executeUpdate();
//            username2.setCellValueFactory(new PropertyValueFactory<Friend, String>("username2"));
//            firstname.setCellValueFactory(new PropertyValueFactory<Friend, String>("firstname"));
//            lastname.setCellValueFactory(new PropertyValueFactory<Friend, String>("lastname"));

            ObservableList<Friend> list2 = load_names();
            table.setItems(list2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Friend> load_names(){
        try{
            //Connection connection = DriverManager.getConnection()
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * from friends");
            ObservableList<Friend> data = FXCollections.observableArrayList();
            while(rs.next()){
                String username1 = rs.getString("username1");
                String username2 = rs.getString("username2");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = loader.load();
                LoginController friendsController = loader.getController();
                String user_looged = friendsController.get_user_logged();

                if(Objects.equals(user_looged, username1)) {
                    ResultSet rs2 = connectDB.createStatement().executeQuery("select * from utilizatori");
                    while(rs2.next()) {
                        String first_name_baza = rs2.getString("first_name");
                        String last_name_baza = rs2.getString("last_name");
                        String username_baza = rs2.getString("username");

                        if(Objects.equals(username_baza, username2)) {
                            Friend utilizator = new Friend(username1, username2, first_name_baza, last_name_baza);
                            data.add(utilizator);
                        }
                    }
                } else if (Objects.equals(user_looged, username2)) {
                    ResultSet rs2 = connectDB.createStatement().executeQuery("select * from utilizatori");
                    while (rs2.next()) {
                        String first_name_baza = rs2.getString("first_name");
                        String last_name_baza = rs2.getString("last_name");
                        String username_baza = rs2.getString("username");

                        if (Objects.equals(username_baza, username1)) {
                            Friend utilizator = new Friend(username2, username1, first_name_baza, last_name_baza);
                            data.add(utilizator);
                        }
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
    void deleteFriend(ActionEvent event) {
        int selectedID = table.getSelectionModel().getSelectedIndex();
        Friend fr = table.getItems().get(selectedID);
        table.getItems().remove(selectedID);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sql = "delete from friends where username1 = ? and username2 = ?";
        String sql2 = "delete from friends where username2 = ? and username1 = ?";
        try{
            PreparedStatement ps = connectDB.prepareStatement(sql);
            ps.setString(1, fr.getUsername1());
            ps.setString(2, fr.getUsername2());
            ps.executeUpdate();

            PreparedStatement ps2 = connectDB.prepareStatement(sql2);
            ps2.setString(1, fr.getUsername1());
            ps2.setString(2, fr.getUsername2());
            ps2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addFriendAction(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("requests.fxml")));
            Stage registerStage = new Stage();
            registerStage.setTitle("Friends suggestion");
            registerStage.setScene(new Scene(root, 600,435));
            registerStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void delete_request(Friend fr){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sql = "delete from requests where username1 = ? and username2 = ?";
        try{
            PreparedStatement ps = connectDB.prepareStatement(sql);
            ps.setString(1, fr.getUsername1());
            ps.setString(2, fr.getUsername2());
            ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Friend> load_requests(){
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            ObservableList<Friend> data = FXCollections.observableArrayList();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            LoginController friendsController = loader.getController();
            String user_looged = friendsController.get_user_logged();

            ResultSet rs = connectDB.createStatement().executeQuery("select * from requests");
            while(rs.next()){
                String username2 = rs.getString("username2");
                String username1 = rs.getString("username1");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                if(user_looged.equals(username2)){
                    Friend fr = new Friend(username1, username2, first_name, last_name);
                    data.add(fr);
                }
            }
            return data;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
