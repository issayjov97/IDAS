/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.studentsstudywindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHelper;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import modules.Podminka;
import modules.Role;
import modules.Predmet;
import modules.PrubehStudia;
import modules.SessionUser;
import modules.Uzivatel;
import sempraceidas2.groupswindow.GroupsMainWindowController;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;
import sempraceidas2.userprofwindow.Zap;

/**
 * FXML Controller class vuyc
 */
public class StudentsStudyWindowController implements Initializable {

    @FXML
    private JFXTextField jmenoField;

    @FXML
    private JFXTextField prijmeniField;

    @FXML
    private JFXTextField idField;

    @FXML
    private TableView<PrubehStudia> tableView;

    @FXML
    private TableColumn<PrubehStudia, String> columnNazevPredmetu;

    @FXML
    private TableColumn<PrubehStudia, String> columnPodminka;

    @FXML
    private TableColumn<PrubehStudia, Zap> columnStav;
    private DatabaseHelper dh;
    private ArrayList<Podminka> podminky;
    private ArrayList<Predmet> predmety;
    @FXML
    private JFXTextField zapocetField;

    @FXML
    private JFXButton zapZapocetBt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initCol();
            dh = DatabaseHelper.getInstance();
            zapZapocetBt.setVisible(true);
            tableView.setEditable(true);
            if(!FXMLDocumentController.isIsAdmin() && !FXMLDocumentController.isIsVyujuici() ){
                zapZapocetBt.setVisible(false);
                tableView.setEditable(false);
            }
            jmenoField.setText(GroupsMainWindowController.getIdUz().getJmeno());
            prijmeniField.setText(GroupsMainWindowController.getIdUz().getPrijmeni());
            idField.setText(GroupsMainWindowController.getIdUz().getId().toString());

            predmety = dh.getPredmetySkupiny(GroupsMainWindowController.getIdSkupiny());
            System.out.println(predmety.size());
            ObservableList<Zap> list = FXCollections.observableArrayList(Zap.values());
            columnStav.setCellFactory(ComboBoxTableCell.<PrubehStudia, Zap>forTableColumn(list));
            columnStav.setOnEditCommit(new EventHandler<CellEditEvent<PrubehStudia, Zap>>() {
                @Override
                public void handle(CellEditEvent<PrubehStudia, Zap> event) {
                    String tmp = event.getNewValue().getCode();
                    PrubehStudia pr = tableView.getSelectionModel().getSelectedItem();
                    pr.setStav(tmp);

                    int status = tmp.equals(Zap.SPLNEN.getCode()) ? 1 : 0;
                    System.out.println(status);
                    System.out.println(pr.getIdPod());
                    dh.updateSplneniPodminky(pr.getIdZap(), pr.getIdPod(), status);
//                    dh.updateSplneniPodminky(status, status, status) //                 
                }
            });
            for (Predmet predmet1 : predmety) {
                int idZapoctu = dh.getIdZapoctu(GroupsMainWindowController.getIdUz().getId(), predmet1);
                System.out.println(idZapoctu);
                if (idZapoctu > 0) {

                    podminky = dh.getPodminkyPredmetu(predmet1.getIdPredmetu());
                    System.out.println("Pdominky:" + podminky.toString());
                    if (!podminky.isEmpty()) {
                        for (Podminka pod : podminky) {

                            System.out.println("===============");
                            System.out.println(pod.getIdPodminy().get());
                            System.out.println("idZap: " + idZapoctu);
                            System.out.println("=====================");
                            int stav = dh.getStavPodminkyStudenta(idZapoctu, pod.getIdPodminy().get());
                            System.out.println("Stav:" + stav);
                            Zap zap = Zap.NESPLNEN;
                            if (stav == 1) {
                                zap = Zap.SPLNEN;
                            }
                            tableView.getItems().add(new PrubehStudia(idZapoctu, pod.getIdPodminy().get(), predmet1.getIdPredmetu(), predmet1.getNazev(), zap.getCode(), pod.getPopis().get()));
                        }
                    }

                }
            }

            tableView.setOnMouseClicked(event -> {
                PrubehStudia prubehStudia = tableView.getSelectionModel().getSelectedItem();
                if (prubehStudia != null) {
                    zapocetField.clear();
                    int stav = dh.getStavZapocet(prubehStudia.getIdZap(), prubehStudia.getPred());
                    if (stav > 0) {
                        Zap zap = stav == 1 ? Zap.SPLNEN : Zap.NESPLNEN;
                        zapocetField.setText(zap.getText());
                    }
                }
            });
        } catch (SQLException ex) {
            Logger.getLogger(StudentsStudyWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initCol() {
        columnNazevPredmetu.setCellValueFactory(new PropertyValueFactory<>("predmet"));
        columnPodminka.setCellValueFactory(new PropertyValueFactory<>("podminka"));
        columnStav.setCellValueFactory(new Callback<CellDataFeatures<PrubehStudia, Zap>, ObservableValue<Zap>>() {
            @Override
            public ObservableValue<Zap> call(CellDataFeatures<PrubehStudia, Zap> param) {
                PrubehStudia prubehStudia = param.getValue();
                String stav = prubehStudia.getStav();

                return new SimpleObjectProperty<>(Zap.getByCode(stav));

            }
        });

    }

    @FXML
    void handleButtonZapsatZapocetAction(ActionEvent event) {
        PrubehStudia pr = tableView.getSelectionModel().getSelectedItem();

        if (pr != null) {
            ChoiceDialog<Zap> dialog = new ChoiceDialog<>();
            dialog.setTitle("Choice Dialog");
            dialog.setHeaderText("Look, a Choice Dialog");
            dialog.setContentText("Choose your letter:");
            dialog.getItems().addAll(Zap.values());
            Optional<Zap> result = dialog.showAndWait();

            if (result.isPresent()) {
                int stav = result.get() == Zap.SPLNEN ? 1 : 0;
                if (dh.updateZapocet(stav, pr.getIdZap(), pr.getPred())) {
                    zapocetField.setText(result.get().getText());
                    WindowHelper.showAlert("Zapocet byl uspesne zapsan!", Alert.AlertType.INFORMATION);
                }

            } else {
                WindowHelper.showAlert("Error", Alert.AlertType.ERROR);
            }
        }

    }

}
