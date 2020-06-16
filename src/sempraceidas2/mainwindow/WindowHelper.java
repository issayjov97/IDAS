/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.mainwindow;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author dzhohar
 */
public class WindowHelper {

    public static void loadWindow(URL loc, String title, Stage parentStage) throws IOException {
        Stage stage;
        Parent parent = FXMLLoader.load(loc);
        if (parentStage != null) {
            stage = parentStage;
        } else {
            stage = new Stage(StageStyle.DECORATED);
        }

        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public static void closeWindow(Event event){
         ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
    public static Alert showAlert(String str, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(str);
        alert.show();
        return alert;

    }
}
