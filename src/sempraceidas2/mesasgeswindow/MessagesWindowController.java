/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.mesasgeswindow;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import modules.SessionUser;
import modules.Uzivatel;

import modules.Zprava;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class MessagesWindowController implements Initializable {

    @FXML
    private JFXListView<Zprava> listZprav;
    @FXML
    private JFXListView<Zprava> listZprav1;
    @FXML
    private JFXTextField najitField;
    DatabaseHelper databaseHelper;
    ObservableList<Zprava> list;
    ObservableList<Zprava> list2;
    @FXML
    private JFXTextField jmenoField;

    @FXML
    private JFXTextField prijmeniField;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            prijmeniField.setEditable(false);
            jmenoField.setEditable(false);
            databaseHelper = DatabaseHelper.getInstance();
            list = FXCollections.observableArrayList();
            list2 = FXCollections.observableArrayList();
            list.addAll(databaseHelper.getZpravy(SessionUser.getUzivatelOnline().getId()));
            list2.addAll(databaseHelper.getPrijateZpravy(SessionUser.getUzivatelOnline().getId()));
            listZprav1.setItems(list2);
            listZprav.setItems(list);
            listZprav.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                Zprava zprava = listZprav.getSelectionModel().getSelectedItem();
                if (zprava != null) {
                    Uzivatel uzivatel = databaseHelper.getAutoraZpravy(zprava.getId());
                    System.out.println(uzivatel.getJmeno());
                    System.out.println(uzivatel.getPrijmeni());
                    jmenoField.setText(uzivatel.getJmeno());
                    prijmeniField.setText(uzivatel.getPrijmeni());
                }

            });
            listZprav1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                Zprava zprava = listZprav1.getSelectionModel().getSelectedItem();
                if (zprava != null) {

                    Uzivatel uzivatel = databaseHelper.getAdresatZpravy(zprava.getId());
                    System.out.println(uzivatel.getJmeno());
                    System.out.println(uzivatel.getPrijmeni());
                    jmenoField.setText(uzivatel.getJmeno());
                    prijmeniField.setText(uzivatel.getPrijmeni());
                }
            });
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleButtonBackAction(ActionEvent event) throws IOException {
        WindowHelper.closeWindow(event);
        WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);

    }


    @FXML
    private void handleButtonNajitAction(ActionEvent event) {
        listZprav.getItems().clear();
        String predmet = najitField.getText();

        for (Zprava zprava : list) {
            System.out.println(zprava);
            if (zprava.getPredmet().equals(predmet)) {
                listZprav.getItems().add(zprava);
            }
        }

    }

    @FXML
    void handleButtonSmazatLeftListAction(ActionEvent event) {
        Zprava zprava = listZprav.getSelectionModel().getSelectedItem();
        if (zprava != null) {

            boolean result = databaseHelper.deleteZpravu1(zprava.getId());
            if (result) {
                list.remove(zprava);
                listZprav.getItems().remove(zprava);
                WindowHelper.showAlert("Zprava byla odstranena", Alert.AlertType.INFORMATION);
            } else {
                WindowHelper.showAlert("Can not delete message", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleButtonSmazatRightListAction(ActionEvent event) {
        Zprava zprava = listZprav1.getSelectionModel().getSelectedItem();
        if (zprava != null) {

            boolean result = databaseHelper.deleteZpravu1(zprava.getId());
            if (result) {
                list2.remove(zprava);
                listZprav1.getItems().remove(zprava);
                WindowHelper.showAlert("Zprava byla odstranena", Alert.AlertType.INFORMATION);
            } else {
                WindowHelper.showAlert("Can not delete message", Alert.AlertType.ERROR);
            }
        }

    }

    void handleButtonSmazatAction(ActionEvent event) {
    }

}
