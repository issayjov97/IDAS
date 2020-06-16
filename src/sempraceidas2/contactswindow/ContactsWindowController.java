/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.contactswindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.crypto.interfaces.DHKey;
import modules.Kontakt;
import modules.ObjektOznameni;
import modules.Oznameni;
import modules.OznameniZmena;
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
public class ContactsWindowController implements Initializable {

    @FXML
    private JFXListView<Uzivatel> listKontakty;
    DatabaseHelper databaseHelper;
    ArrayList<Kontakt> kontakty;
    ArrayList<Uzivatel> uzivatele;
    @FXML
    private MenuItem s1;
    private static Uzivatel uzivatel;

    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            imageView.setImage(new Image("file:src/images/chat.png"));

            databaseHelper = DatabaseHelper.getInstance();
            uzivatele = databaseHelper.getFriends(SessionUser.getUzivatelOnline().getId());
            if (!uzivatele.isEmpty()) {
                listKontakty.getItems().addAll(uzivatele);
            }

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
    private void handleItem1ButtonAction(ActionEvent event) {
        uzivatel = listKontakty.getSelectionModel().getSelectedItem();
        if (uzivatel != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            VBox box = new VBox();
            HBox hbox1 = new HBox();
            HBox hbox2 = new HBox();
            HBox hbox3 = new HBox();
            hbox1.setSpacing(15);
            hbox2.setSpacing(15);
            hbox3.setSpacing(15);
            box.setSpacing(10);

            Label predmet = new Label("Predmet:");
            TextArea text = new TextArea();
            Label poznamkaLabel = new Label("Poznamka:");
            TextField textpoznamka = new TextField();
            TextField predmetField = new TextField();
            alert.setHeaderText("Nova zprava");
            hbox1.getChildren().addAll(predmet, predmetField);
            hbox2.getChildren().addAll(poznamkaLabel, textpoznamka);
            hbox3.getChildren().add(text);
            box.getChildren().addAll(hbox1, hbox2, hbox3);
            alert.getDialogPane().setContent(box);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK && !text.getText().isEmpty()) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                int idZpravy = databaseHelper.getID("zpravy_sq");
                Zprava zprava = new Zprava(idZpravy, timestamp, text.getText(), textpoznamka.getText(), predmetField.getText());
                databaseHelper.insertMessage(idZpravy, text.getText(), textpoznamka.getText(), timestamp, predmetField.getText());
                databaseHelper.insertZpravyKontakty(SessionUser.getUzivatelOnline().getId(), idZpravy, uzivatel.getId());
                int idObjk = databaseHelper.getID("OBJEKT_OZNAMENI_SEQ");
                int oznId = databaseHelper.getID("oznameni_SEQ");
                int oznZmId = databaseHelper.getID("oznameni_zmena_SEQ");
                ObjektOznameni objektOznameni = new ObjektOznameni(idObjk, 1, zprava.getId(), new Timestamp(System.currentTimeMillis()), 0);
                databaseHelper.addOznameniObjekt(objektOznameni);
                Oznameni oznameni = new Oznameni(oznId, objektOznameni.getId(), uzivatel.getId(), 0);
                databaseHelper.addOznameni(oznameni);
                OznameniZmena oznameniZm = new OznameniZmena(oznZmId, objektOznameni.getId(), SessionUser.getUzivatelOnline().getId(), 0);
                databaseHelper.addOznameniZmena(oznameniZm);
                WindowHelper.showAlert("Zprava byla uspesne odelsana", Alert.AlertType.INFORMATION);

            }
        } else {
            WindowHelper.showAlert("Error):", Alert.AlertType.ERROR);

        }
    }

    @FXML
    private void handleItem2ButtonAction(ActionEvent event) {
        uzivatel = listKontakty.getSelectionModel().getSelectedItem();
        if (uzivatel != null) {
            uzivatele.remove(uzivatel);
            databaseHelper.deleteKontact(uzivatel.getId(), SessionUser.getUzivatelOnline().getId());
            databaseHelper.deleteKontact(SessionUser.getUzivatelOnline().getId(), uzivatel.getId());
            listKontakty.getItems().remove(uzivatel);
        }

    }

    @FXML
    void handleItem3ProfileAction(ActionEvent event) {
        try {
            uzivatel = listKontakty.getSelectionModel().getSelectedItem();
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/userprofwindow/UserProfile.fxml"), "Profile", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static Uzivatel getUzivatel() {
        if (uzivatel != null) {
            return uzivatel;

        }
        return null;
    }

}
