/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.loginwindow;

import database.DBChangeNotification;
import database.DatabaseHelper;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import sempraceidas2.mainwindow.WindowHelper;

/**
 *
 * @author dzhohar
 */
public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sempraceidas2/loginwindow/LoignWindow.fxml"));
       
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatabaseHelper.getInstance();

                } catch (SQLException ex) {
                    WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
                    Platform.exit();
                }
            }
        }).start();
        stage.setOnCloseRequest(e -> {
            try {
                DatabaseHelper.closeConnection();
                System.out.println("connection closed");
            } catch (SQLException exc) {
                System.err.println("couldn't close connection");
            }
        });
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
