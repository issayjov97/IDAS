/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.groupswindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import modules.Komentar;
import modules.ObjektOznameni;
import modules.Oznameni;
import modules.OznameniZmena;
import modules.Role;
import modules.SessionUser;
import modules.Udalost;
import modules.Uzivatel;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class GroupsWindowController implements Initializable {

    @FXML
    private TreeTableView<Komentar> tableTreeView;
    @FXML
    private TreeTableColumn<Komentar, String> nazevKom;

    @FXML
    private TreeTableColumn<Komentar, String> obsahKom;

    @FXML
    private TreeTableColumn<Komentar, Timestamp> datumKom;
    private TreeItem<Komentar> root;
    private TreeItem<Komentar> tmp;
    private DatabaseHelper dh;
    private ArrayList<Komentar> komentare;
    @FXML
    private JFXButton odebrBut;

    @FXML
    private ImageView imageView;
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            imageView.setImage(new Image("file:src/images/blogging.png"));

            odebrBut.setVisible(false);
            odebrBut.setDisable(true);
            if (FXMLDocumentController.isIsAdmin() || FXMLDocumentController.isIsVyujuici()) {
                odebrBut.setVisible(true);
                odebrBut.setDisable(false);
            }
            dh = DatabaseHelper.getInstance();
            komentare = new ArrayList<>();
            fillTable();

            obsahKom.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            obsahKom.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Komentar, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Komentar, String> event) {
                    String tmp = event.getNewValue();
                    Komentar komentar = tableTreeView.getSelectionModel().getSelectedItem().getValue();
                    komentar.setObsah(tmp);
                    dh.changeObsahKomentare(komentar.getIdKom().get(), tmp);
                }
            });
                Callback<TreeTableColumn<Komentar, String>, TreeTableCell<Komentar, String>> defaultTextFieldCellFactory
                    = TextFieldTreeTableCell.<Komentar>forTreeTableColumn();

            nazevKom.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                nazevKom.setCellFactory(col -> {
                    System.out.println("Called");
                    TreeTableCell<Komentar, String> cell = defaultTextFieldCellFactory.call(col);
                    cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                        TreeTableRow row = cell.getTreeTableRow();
                        if (row == null) {
                            cell.setEditable(false);
                        } else {
                            Komentar item = (Komentar) cell.getTreeTableRow().getItem();
                            if (item.getNazev().get().equals("sdfs")) {
                                System.out.println("editable true");
                                cell.setEditable(true);
                            } else {
                                cell.setEditable(false);
                            }
                        }

                    });
                    return cell;
                });
            });
        
            nazevKom.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Komentar, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Komentar, String> event) {
                    String tmp = event.getNewValue();
                    Komentar komentar = tableTreeView.getSelectionModel().getSelectedItem().getValue();
                    komentar.setNazev(tmp);
                    dh.changeNazevKomentare(komentar.getIdKom().get(), tmp);
                }
            });

            nazevKom.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            nazevKom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Komentar, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Komentar, String> param) {
                    return param.getValue().getValue().getNazev();
                }
            });
            obsahKom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Komentar, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Komentar, String> param) {
                    return param.getValue().getValue().getObsah();
                }
            });
            datumKom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Komentar, Timestamp>, ObservableValue<Timestamp>>() {
                @Override
                public ObservableValue<Timestamp> call(TreeTableColumn.CellDataFeatures<Komentar, Timestamp> param) {
                    return param.getValue().getValue().getDate();
                }
            });

            tableTreeView.refresh();
            tableTreeView.setEditable(true);

            tableTreeView.setShowRoot(true);
            tableTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                       TreeItem<Komentar> treeItem = tableTreeView.getSelectionModel().getSelectedItem();
                        if (treeItem != null) {
                            String tmp = dh.getAuToraKomentare(treeItem.getValue().getIdKom().get());
                            String pole[] = tmp.split(";");
                            nameField.setText(pole[0]);
                            surnameField.setText(pole[1]);
                        }

                    }
                }

            });

        } catch (SQLException ex) {
            Logger.getLogger(GroupsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleButtonBackAction(ActionEvent event) {
        try {

            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/groupswindow/GroupsMainWindow.fxml"), "Moje Skupiny", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void handleButtonOdebratAction(ActionEvent event) {
        TreeItem<Komentar> item = tableTreeView.getSelectionModel().getSelectedItem();

        Komentar komentar = item.getValue();
        ArrayList<Komentar> komentares = dh.getPodKomentars(komentar.getIdKom().get());
        for (int i = komentares.size() - 1; i >= 0; i--) {
            dh.odstranitKomentar(komentares.get(i).getIdKom().get());
        }
        if (tableTreeView.getRoot().getValue().getIdKom().get() == item.getValue().getIdKom().get()) {
            dh.setNullSkupKomentar(item.getValue().getIdKom().get());
            dh.odstranitKomentar(tableTreeView.getRoot().getValue().getIdKom().get());

            tableTreeView.setRoot(null);
        } else {
            item.getParent().getChildren().remove(item);

        }
    }

    @FXML
    void handleButtonNapsatKomentarAction(ActionEvent event) {
        int kom = dh.getSkupKomentar(GroupsMainWindowController.getIdSkupiny());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        VBox box = new VBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        hbox2.setSpacing(15);
        hbox3.setSpacing(15);
        box.setSpacing(10);

        Label obsahLabel = new Label("Obsah:");
        Label nazevLabel = new Label("Nazev:");

        TextField obsahField = new TextField();
        TextField nazevField = new TextField();
        alert.setHeaderText("Nastaveni");

        hbox2.getChildren().addAll(obsahLabel, obsahField);
        hbox3.getChildren().addAll(nazevLabel, nazevField);
        box.getChildren().addAll(hbox2, hbox3);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK && !obsahField.getText().isEmpty() && !nazevField.getText().isEmpty()) {
            int idKom = dh.getID("KOMENTARE_SQ");
            Komentar komentar = new Komentar(idKom, nazevField.getText(), obsahField.getText(),
                    new Timestamp(new Date().getTime()), 0, 1, SessionUser.getUzivatelOnline().getId(), 0);
            dh.pridatKomentar(komentar);
            if (kom == 0) {
                komentar.setIdPodKom(0);
                dh.pridatKomentarSkupine(komentar.getIdKom().get(), GroupsMainWindowController.getIdSkupiny());
                tableTreeView.setRoot(new TreeItem<>(komentar));
            } else {
                TreeItem<Komentar> item = tableTreeView.getSelectionModel().getSelectedItem();
                if (item != null) {
                    System.out.println(item.getValue().getIdKom().get());
                    komentar.setIdPodKom(item.getValue().getIdKom().get());
                    dh.pridatPodKomentar(item.getValue().getIdKom().get(), komentar.getIdKom().get());
                    item.getChildren().add(new TreeItem<>(komentar));
                }

                ArrayList<Uzivatel> uzivatele = dh.getClenoveSkupiny(GroupsMainWindowController.getIdSkupiny());
                if (uzivatele.size() > 0) {
                    int idObjk = dh.getID("OBJEKT_OZNAMENI_SEQ");
                    ObjektOznameni objektOznameni = new ObjektOznameni(idObjk, 3,   GroupsMainWindowController.getIdSkupiny(), komentar.getDate().get(), 0);
                    dh.addOznameniObjekt(objektOznameni);
                    uzivatele.stream().map((uzivatel) -> {
                        int oznId = dh.getID("oznameni_SEQ");
                        Oznameni oznameni = new Oznameni(oznId, objektOznameni.getId(), uzivatel.getId(), 0);
                        return oznameni;
                    }).forEachOrdered((oznameni) -> {
                        dh.addOznameni(oznameni);
                    });

                    int oznZmId = dh.getID("oznameni_zmena_SEQ");
                    OznameniZmena oznameniZm = new OznameniZmena(oznZmId, objektOznameni.getId(), komentar.getIdUzivatele(), 0);
                    dh.addOznameniZmena(oznameniZm);
                }

            }

        }

    }

    private void fillTable() {
        int idKom = GroupsMainWindowController.getIdKomSkupiny();
        if (idKom != 0) {
            komentare.addAll(dh.getPodKomentars(GroupsMainWindowController.getIdKomSkupiny()));

        }
        if (!komentare.isEmpty()) {
            TreeItem<Komentar> child = null;
            for (Komentar km1 : komentare) {
                int indexAktRoot = 0;
                if (km1.getIdPodKom().get() == 0) {
                    root = new TreeItem<>(km1);
                    tableTreeView.setRoot(root);
                } else {

                }
                for (Komentar km2 : komentare) {

                    if (km2.getIdPodKom().get() == km1.getIdKom().get()) {
                        child = new TreeItem<>(km2);
                        root.getChildren().add(child);
                        if (indexAktRoot == 0) {
                            tmp = child;
                        }
                        indexAktRoot++;
                    }
                }
                root = tmp;

            }

        }

    }

    @FXML
    void handleButtonInfoAction(ActionEvent event) {
        TreeItem<Komentar> item = tableTreeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                Komentar komentar = item.getValue();
                if (komentar.getIdUzivatele() != 0) {
                    Uzivatel uzivatel = dh.getUserById(komentar.getIdUzivatele());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    VBox box = new VBox();
                    HBox hbox2 = new HBox();
                    HBox hbox3 = new HBox();
                    hbox2.setSpacing(15);
                    hbox3.setSpacing(15);
                    box.setSpacing(10);

                    Label obsahLabel = new Label();
                    Label nazevLabel = new Label();
                    obsahLabel.setText("Jmeno: " + uzivatel.getJmeno());
                    nazevLabel.setText("Prijmeni: " + uzivatel.getPrijmeni());

                    alert.setHeaderText("Autor");

                    hbox2.getChildren().addAll(obsahLabel);
                    hbox3.getChildren().addAll(nazevLabel);
                    box.getChildren().addAll(hbox2, hbox3);
                    alert.getDialogPane().setContent(box);
                    alert.showAndWait();
                } else {
                    WindowHelper.showAlert("Autor nebyl nalezen", Alert.AlertType.INFORMATION);
                }

            } catch (SQLException ex) {
                WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            }
        }

    }
}
