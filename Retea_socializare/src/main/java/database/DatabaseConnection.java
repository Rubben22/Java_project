package database;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String url = "jdbc:postgresql://localhost:5432/socialnetwork5";

        try{
            //Class.forName("com.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, "postgres", "rubenubb");

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}
