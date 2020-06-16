/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.podminkywindow;

import com.jfoenix.controls.JFXListView;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javax.crypto.interfaces.DHKey;
import modules.Podminka;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class PodminkyWindowController implements Initializable {

    @FXML
    private JFXListView<Podminka> listPodminky;

    private DatabaseHelper databaseHelper;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            databaseHelper = DatabaseHelper.getInstance();
            ArrayList<Podminka> podminky = databaseHelper.getAllPodminky();
            if (!podminky.isEmpty()) {
                listPodminky.getItems().addAll(podminky);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PodminkyWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleButtonOdebratAction(ActionEvent event) {
        Podminka pod = listPodminky.getSelectionModel().getSelectedItem();
        if (pod != null) {
            databaseHelper.deletePodminku(pod.getIdPodminy().get());
            listPodminky.getItems().remove(pod);
        }

    }

    @FXML
    void handleButtonPridatAction(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("New Podminka");
        dialog.setContentText("Popis");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int id = databaseHelper.getID("PODMINKY_ZAPOCTU_SQ");
            Podminka pod = new Podminka(id, result.get());
            databaseHelper.pridatNovouPodminku(pod);
            listPodminky.getItems().add(pod);
        }
    }

    @FXML
    void handleButtonBackAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);

        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
