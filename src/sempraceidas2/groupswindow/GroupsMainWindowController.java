/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.groupswindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.Podminka;
import modules.Predmet;
import modules.SessionUser;
import modules.Skupina;
import modules.StudijniPlan;
import modules.Uzivatel;
import modules.Zapocet;
import oracle.jdbc.driver.Message;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;

/**
 *
 * @author dzhohar
 */
public class GroupsMainWindowController implements Initializable {

    @FXML
    private JFXListView<Skupina> skList;
    DatabaseHelper dh;
    static int idKom;
    private ArrayList<StudijniPlan> plany;
    private static int idSkupiny;
    private static Uzivatel idUz;
    @FXML
    private JFXButton odebrBut;

    @FXML
    private JFXButton pridButton;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXListView<Uzivatel> clenoveSkupiny;
    @FXML
    private MenuItem menuItemAddUser;
    @FXML
    private MenuItem menuItemAddSubject;
    @FXML
    private MenuItem menuItemDeleteUser;
    @FXML
    private BorderPane bordPn;
    @FXML
    private MenuItem menuItemMore;

    public static Uzivatel getIdUz() {
        return idUz;
    }

    public static void setIdUz(Uzivatel idUz) {
        GroupsMainWindowController.idUz = idUz;
    }

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        try {
            skList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        Skupina skupina = skList.getSelectionModel().getSelectedItem();
                        if (skupina != null) {
                            clenoveSkupiny.getItems().clear();
                            clenoveSkupiny.getItems().addAll(dh.getClenoveSkupiny(skupina.getId()));
                        }

                    }
                }

            });
            pridButton.setVisible(false);
            odebrBut.setVisible(false);
            pridButton.setDisable(true);
            odebrBut.setDisable(true);
            imageView.setImage(new Image("file:src/images/group.png"));
            dh = DatabaseHelper.getInstance();
            if (FXMLDocumentController.isIsAdmin()) {
                pridButton.setVisible(true);
                odebrBut.setVisible(true);
                pridButton.setDisable(false);
                odebrBut.setDisable(false);
                skList.getItems().addAll(dh.getAllSkupiny());
            } else if (FXMLDocumentController.isIsVyujuici() && !FXMLDocumentController.isIsAdmin()) {
                skList.getItems().addAll(dh.getSkupinyVyucujiciho(SessionUser.getUzivatelOnline().getId()));
                pridButton.setVisible(true);
                odebrBut.setVisible(true);
                pridButton.setDisable(false);
                odebrBut.setDisable(false);
            } else {
                menuItemAddUser.setVisible(false);
                menuItemDeleteUser.setVisible(false);
                menuItemAddUser.setDisable(true);
                menuItemDeleteUser.setDisable(true);
                menuItemAddSubject.setVisible(false);
                menuItemAddSubject.setDisable(true);
                
                skList.getItems().addAll(dh.getSkupiny(SessionUser.getUzivatelOnline().getId()));
            }

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
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

    @FXML
    void handleMenuContextItem1Action(ActionEvent event) {
        try {
            Skupina skupina = skList.getSelectionModel().getSelectedItem();
            if (skupina != null) {
                idKom = skupina.getKomentar();
                idSkupiny = skupina.getId();
                Stage stage = (Stage) bordPn.getScene().getWindow();
                stage.close();
                WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/groupswindow/GroupsWindow.fxml"), "MainWindow", null);

            } else {
                WindowHelper.showAlert("Nothing to do", Alert.AlertType.INFORMATION);
            }

        } catch (IOException ex) {
            Logger.getLogger(GroupsMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleMenuContextItem2Action(ActionEvent event) {
//        Skupina skupina = skList.getSelectionModel().getSelectedItem();
//        if (skupina != null) {
//
////            String tmp = dh.getPredmetySkupiny(skupina.getId());
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            VBox box = new VBox();
//            HBox hbox1 = new HBox();
//            HBox hbox2 = new HBox();
//            HBox hbox4 = new HBox();
//            hbox1.setSpacing(15);
//            hbox2.setSpacing(15);
//            hbox4.setSpacing(15);
//            box.setSpacing(10);
//            Label planLabel = new Label();
//
//            Label predmetLabel = new Label();
//
//            if (tmp.length() > 0) {
//                tmp = tmp.replace("null", "");
//                tmp = tmp.replaceFirst(",", "");
//                predmetLabel.setText(tmp);
//                hbox4.getChildren().addAll(planLabel);
//                hbox2.getChildren().addAll(predmetLabel);
//                box.getChildren().addAll(hbox4, hbox2);
//                alert.getDialogPane().setContent(box);
//
//            } else {
//                alert.setContentText("Skupina nema prideleny zadny predmet");
//            }
//
//            alert.setHeaderText("Skupiny");
//            alert.showAndWait();
//        } else {
//            WindowHelper.showAlert("Nothing to do", Alert.AlertType.INFORMATION);
//        }
    }

    @FXML
    void handleButtonOdebratkAction(ActionEvent event) {
        Skupina skupina = skList.getSelectionModel().getSelectedItem();
        if (skupina != null) {
            dh.odstranitSkupinu(skupina.getId());
            skList.getItems().remove(skupina);
        }

    }

    public static int getIdKomSkupiny() {

        return idKom;
    }

    public static int getIdSkupiny() {

        return idSkupiny;
    }

    @FXML
    void handleButtonPridatAction(ActionEvent event) {
        ArrayList<Predmet> predmety = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        VBox box = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        box.setSpacing(10);
        Label nazevLabel = new Label("Nazev skupiny: ");

        Label predmetLabel = new Label("Predmet: ");
        TextField nazevField = new TextField();
        ComboBox<Predmet> predmetyBox = new ComboBox<>();
        if (FXMLDocumentController.isIsVyujuici() && !FXMLDocumentController.isIsAdmin()) {
            predmety = dh.getPredmetyVyucujiciho(SessionUser.getUzivatelOnline().getId());
        } else {
            predmety = dh.getPredmety();
        }
        predmetyBox.getItems().addAll(predmety);

        hbox2.getChildren().addAll(predmetLabel, predmetyBox);
        hbox1.getChildren().addAll(nazevLabel, nazevField);
        box.getChildren().addAll(hbox1, hbox2);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK && !nazevField.getText().isEmpty() && !nazevField.getText().equals("") && predmetyBox.getValue() != null) {
            Predmet predmet = predmetyBox.getValue();

            int idSkup = dh.getID("SKUPINY_SQ");
            Skupina skupina = new Skupina(idSkup, nazevField.getText(), 0);
            ArrayList<Integer> idPlanu = dh.getPlanPedmetu(predmet.getIdPredmetu());
            if (!idPlanu.isEmpty()) {
                dh.pridatSkupiny(skupina);
                dh.pridatPredmetySkupiny(skupina.getId(), predmet.getIdPredmetu());
                skList.getItems().add(skupina);
            } else {
                WindowHelper.showAlert("Predmet nema nastaveny plan", Alert.AlertType.WARNING);
            }
        } else {
            WindowHelper.showAlert("Prazdne pole!", Alert.AlertType.WARNING);
        }

    }

    @FXML
    void handleMenuContextItemAddUserAction(ActionEvent event) {
        Skupina skupina = skList.getSelectionModel().getSelectedItem();
        if (skupina != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            VBox box = new VBox();
            HBox hbox1 = new HBox();
            HBox hbox2 = new HBox();
            hbox1.setSpacing(15);
            hbox2.setSpacing(15);
            box.setSpacing(10);
            Label nazevLabel = new Label("Vybrat studenta: ");

            ComboBox<Uzivatel> uzivateleBox = new ComboBox<>();
            System.out.println(uzivateleBox.getItems().size());
            uzivateleBox.getItems().addAll(dh.getNoveClenySkupiny(skupina.getId()));
            hbox1.getChildren().addAll(nazevLabel);
            hbox2.getChildren().addAll(uzivateleBox);
            box.getChildren().addAll(hbox1, hbox2);
            alert.getDialogPane().setContent(box);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK && uzivateleBox.getValue() != null) {
                dh.pridatClenaSkupiny(skupina.getId(), uzivateleBox.getValue().getId());
                clenoveSkupiny.getItems().add(uzivateleBox.getValue());

                ArrayList<Predmet> predmety = dh.getPredmetySkupiny(skupina.getId());

                if (!predmety.isEmpty()) {
                    for (Predmet predmet : predmety) {
                        int idZap = dh.getID("ZAPOCTY_SQ");
                        Zapocet zapocet = new Zapocet(idZap, new Timestamp(new Date().getTime()), 0, uzivateleBox.getValue().getId());
                        dh.addZapocet(zapocet, predmet.getIdPredmetu());
                        ArrayList<Podminka> podminky = dh.getPodminkyPredmetu(predmet.getIdPredmetu());
                        for (Podminka podminka : podminky) {
                        dh.addStavSplneniPodminekPredmetu(zapocet, podminka);    
                        }
                        

                    }

                }

            }
        } else {
            WindowHelper.showAlert("Nothing to do", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void handleMenuContextItemDeleteUserAction(ActionEvent event) {
        Uzivatel uzivatel = clenoveSkupiny.getSelectionModel().getSelectedItem();
        Skupina skupina = skList.getSelectionModel().getSelectedItem();

        if (uzivatel != null && skupina != null) {
            if (dh.odstranitClenaSkupiny(uzivatel.getId(), skupina.getId())) {
                clenoveSkupiny.getItems().remove(uzivatel);
            }
        } else {
            WindowHelper.showAlert("Error", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleMenuContextItemAddSubjectAction(ActionEvent event) {
        Skupina skupina = skList.getSelectionModel().getSelectedItem();
        if (skupina != null) {
            ArrayList<Predmet> predmety = dh.getPredmetySkupinyById(skupina.getId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            VBox box = new VBox();
            HBox hbox2 = new HBox();
            hbox2.setSpacing(15);
            box.setSpacing(10);
            Label predmetLabel = new Label("Predmet: ");
            ComboBox<Predmet> predmetyBox = new ComboBox<>();
            predmetyBox.getItems().addAll(predmety);
            hbox2.getChildren().addAll(predmetLabel, predmetyBox);
            box.getChildren().addAll(hbox2);
            alert.getDialogPane().setContent(box);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK && predmetyBox.getValue() != null) {
                ArrayList<Integer> planyPredmetu = dh.getPlanPedmetu(predmetyBox.getValue().getIdPredmetu());
                if (planyPredmetu.isEmpty()) {
                    WindowHelper.showAlert("Predmet nema nastaveny plan", Alert.AlertType.WARNING);
                } else {
                    dh.pridatPredmetySkupiny(skupina.getId(), predmetyBox.getValue().getIdPredmetu());
                    
                }
            }

        } else {
            WindowHelper.showAlert("Nothing to do", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void handleMenuContextItemMorerAction(ActionEvent event) {
        try {
            Skupina skupina = skList.getSelectionModel().getSelectedItem();
            Uzivatel uzivatel = clenoveSkupiny.getSelectionModel().getSelectedItem();
            if (skupina != null && uzivatel != null) {
                idSkupiny = skupina.getId();
                idUz = uzivatel;
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/studentsstudywindow/StudentsStudyWindow.fxml"), "Prubeh studia", null);

            }
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }
}
