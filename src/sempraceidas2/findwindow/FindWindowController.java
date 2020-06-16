/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.findwindow;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import database.Const;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import modules.Udalost;
import modules.Uzivatel;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import database.DatabaseHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.converter.LocalDateTimeStringConverter;
import modules.Kontakt;
import modules.ObjektOznameni;
import modules.Oznameni;
import modules.OznameniZmena;
import modules.Role;
import modules.SessionUser;
import modules.StudijniObor;
import modules.StudijniPlan;
import modules.Udalost;
import modules.Uzivatel;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class FindWindowController implements Initializable {

    @FXML
    private JFXTextField prijField;
    @FXML
    private JFXTextField jmenoField;
    @FXML
    private JFXTextField idField;

    private ObservableList<Uzivatel> list;
    private DatabaseHelper databaseHelper;
    @FXML
    private TableView<Uzivatel> tableView;
    @FXML
    private TableColumn<Uzivatel, String> jmenoColumn;
    @FXML
    private TableColumn<Uzivatel, String> prijmeniColumn;
    @FXML
    private TableColumn<Uzivatel, String> emailColumn;
    @FXML
    private TableColumn<Uzivatel, Integer> idCol;
    @FXML
    private TableColumn<Uzivatel, String> loginCl;
    @FXML
    private TableColumn<Uzivatel, String> hesloCl;
    @FXML
    private TableColumn<Uzivatel, String> poznamCl;

    @FXML
    private TableColumn<Uzivatel, Integer> blokaceCl;

    @FXML
    private JFXToggleButton toggleBt;
    @FXML
    private JFXButton blockBt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            databaseHelper = DatabaseHelper.getInstance();
            blockBt.setVisible(false);
            blockBt.setDisable(true);
            toggleBt.setVisible(false);
            toggleBt.setDisable(true);
            tableView.setEditable(true);
            idCol.setVisible(false);
            blokaceCl.setVisible(false);
            loginCl.setVisible(false);
            hesloCl.setVisible(false);
            list = FXCollections.observableArrayList();
            tableView.setItems(list);
            initCol();

            if (FXMLDocumentController.isIsAdmin()) {
                blockBt.setVisible(true);
                blockBt.setDisable(false);
                toggleBt.setVisible(true);
                toggleBt.setDisable(false);
                idCol.setVisible(true);
                blokaceCl.setVisible(true);
                loginCl.setVisible(true);
                hesloCl.setVisible(true);
                idCol.setPrefWidth(80);
                blokaceCl.setPrefWidth(80);
                loginCl.setPrefWidth(100);
                hesloCl.setPrefWidth(100);
                poznamCl.setCellFactory((TextFieldTableCell.forTableColumn()));
                blokaceCl.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                loginCl.setCellFactory((TextFieldTableCell.forTableColumn()));
                hesloCl.setCellFactory((TextFieldTableCell.forTableColumn()));
                emailColumn.setCellFactory((TextFieldTableCell.forTableColumn()));
                prijmeniColumn.setCellFactory((TextFieldTableCell.forTableColumn()));
                jmenoColumn.setCellFactory((TextFieldTableCell.forTableColumn()));
                poznamCl.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setPoznamka(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserPoznamka(uzivatel.getId(), tmp);
                });
                hesloCl.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setHeslo(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserHeslo(uzivatel.getId(), tmp);
                });
                loginCl.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setHeslo(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserLogin(uzivatel.getId(), tmp);
                });

                jmenoColumn.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setJmeno(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserJmeno(uzivatel.getId(), tmp);
                });
                prijmeniColumn.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setPrijmeni(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserPrijmeni(uzivatel.getId(), tmp);
                });
                blokaceCl.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    int tmp = e.getNewValue();
                    uzivatel.setBlokace(new SimpleIntegerProperty(tmp));
                    databaseHelper.changeUserBlokace(uzivatel.getId(), tmp);
                });
                emailColumn.setOnEditCommit(e -> {
                    Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
                    String tmp = e.getNewValue();
                    uzivatel.setEmail(new SimpleStringProperty(tmp));
                    databaseHelper.changeUserEmail(uzivatel.getId(), tmp);
                });
                toggleBt.setOnAction((event) -> {
                    if (toggleBt.isSelected()) {
                        list.clear();
                        list.setAll(databaseHelper.getAllUsers());

                    } else {
                        list.clear();
                    }
                });
            }

        } catch (SQLException ex) {
            Logger.getLogger(FindWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButtonBackAction(ActionEvent event) throws IOException {
        WindowHelper.closeWindow(event);
        WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);

    }

    @FXML
    private void handleButtonFindAction(ActionEvent event) {
        String jmeno = jmenoField.getText();
        String primeni = prijField.getText();
        int id = idField.getText().matches("[0-9]+") ? Integer.valueOf(idField.getText()) : 0;
        idField.clear();
        jmenoField.clear();
        prijField.clear();
        if ((jmeno.isEmpty() || jmeno.equals("")) && (primeni.equals("") || primeni.isEmpty()) && id == 0) {
            WindowHelper.showAlert("Prazdne pole!!!", Alert.AlertType.WARNING);
        } else {
            tableView.getItems().clear();
            ArrayList<Uzivatel> uzivatele = databaseHelper.findUser(jmeno, primeni, id);
            uzivatele.forEach((uzivatel) -> {
                tableView.getItems().add(uzivatel);
            });
            if (uzivatele.isEmpty()) {
                WindowHelper.showAlert("?---؟", Alert.AlertType.INFORMATION);
            }

        }

    }

    private void initCol() {
        jmenoColumn.setCellValueFactory(new PropertyValueFactory<>("jmeno"));
        prijmeniColumn.setCellValueFactory(new PropertyValueFactory<>("prijmeni"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginCl.setCellValueFactory(new PropertyValueFactory<>("login"));
        hesloCl.setCellValueFactory(new PropertyValueFactory<>("heslo"));
        poznamCl.setCellValueFactory(new PropertyValueFactory<>("poznamka"));
        blokaceCl.setCellValueFactory(new PropertyValueFactory<>("blokace"));

    }

    @FXML
    void handleButtonOdebratUzivAction(ActionEvent event) {
        Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
        if (uzivatel != null) {
            databaseHelper.odebratUzivatele(uzivatel.getId());
            tableView.getItems().remove(uzivatel);
        }
    }

    @FXML
    private void handleButtonPridatAction(ActionEvent event) {
        Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();

        if (uzivatel != null) {
            int res = databaseHelper.callFunctionAreFriends(SessionUser.getUzivatelOnline().getId(), uzivatel.getId());
            if (res == 0) {
                int idObjk = databaseHelper.getID("OBJEKT_OZNAMENI_SEQ");
                int oznId = databaseHelper.getID("oznameni_SEQ");
                int oznZmId = databaseHelper.getID("oznameni_zmena_SEQ");
                ObjektOznameni objektOznameni = new ObjektOznameni(idObjk, 4, oznId, new Timestamp(System.currentTimeMillis()), 0);
                databaseHelper.addOznameniObjekt(objektOznameni);
                Oznameni oznameni = new Oznameni(oznId, objektOznameni.getId(), uzivatel.getId(), 0);
                databaseHelper.addOznameni(oznameni);
                OznameniZmena oznameniZm = new OznameniZmena(oznZmId, objektOznameni.getId(), SessionUser.getUzivatelOnline().getId(), 0);
                databaseHelper.addOznameniZmena(oznameniZm);
                WindowHelper.showAlert("Zadost byla odeslana", Alert.AlertType.INFORMATION);
            }
            else{
                 WindowHelper.showAlert("Zadost uz byla odeslana", Alert.AlertType.INFORMATION);
            }

        } else {
            WindowHelper.showAlert("Vyberte uzivatele", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleButtonZablokovatAction(ActionEvent event) {
        Uzivatel uzivatel = tableView.getSelectionModel().getSelectedItem();
        if (uzivatel != null && uzivatel.getBlokace() != 0) {
            uzivatel.setBlokace(new SimpleIntegerProperty(0));
            boolean res = databaseHelper.zablokovatUzivatele(uzivatel.getId());
            if (res) {
                WindowHelper.showAlert("Uzivatel byl zablokovan", Alert.AlertType.INFORMATION);
            } else {
                WindowHelper.showAlert("Error", Alert.AlertType.ERROR);

            }
        }
    }

}
