/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.mainwindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sempraceidas2.contactswindow.ContactsWindowController;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class DrawerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void handleKontaktyAction(ActionEvent event) throws IOException {
        WindowHelper.closeWindow(event);
        WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/contactswindow/ContactsWindow.fxml"), "Moje kontakty", null);

    }

    @FXML
    void handleSkupinyAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/groupswindow/GroupsMainWindow.fxml"), "Moje Skupiny", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void handleNajitAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/findwindow/FindWindow.fxml"), "Najit uzivatele", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void handleZpravyAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mesasgeswindow/MessagesWindow.fxml"), "Zpravy", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }
}
