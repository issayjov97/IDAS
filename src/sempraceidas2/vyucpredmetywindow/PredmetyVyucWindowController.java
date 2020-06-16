/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.vyucpredmetywindow;

import com.jfoenix.controls.JFXListView;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import modules.Predmet;
import modules.SessionUser;
import modules.Skupina;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class PredmetyVyucWindowController implements Initializable {

    @FXML
    private JFXListView<Predmet> predmetyList;

    @FXML
    private JFXListView<Skupina> skupinyList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DatabaseHelper helper = DatabaseHelper.getInstance();
            ArrayList<Predmet> predmety = helper.getPredmetyVyucujiciho(SessionUser.getUzivatelOnline().getId());
            if (!predmety.isEmpty()) {
                predmetyList.getItems().addAll(predmety);
            }
            predmetyList.setOnMouseClicked(e -> {
                Predmet predmet = predmetyList.getSelectionModel().getSelectedItem();
                if (predmet != null) {
                    ArrayList<Skupina> skupiny = helper.getSkupinyPredmetu(predmet.getIdPredmetu());
                        if (!skupinyList.getItems().isEmpty()) {
                            skupinyList.getItems().clear();
                            
                        }
                    
                    if (!skupiny.isEmpty()) {
                   
                        skupinyList.getItems().addAll(skupiny);
                    }
                }
            });
        } catch (SQLException ex) {
            Logger.getLogger(PredmetyVyucWindowController.class.getName()).log(Level.SEVERE, null, ex);
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
