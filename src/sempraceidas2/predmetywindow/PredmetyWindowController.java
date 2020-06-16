/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.predmetywindow;

import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import javax.crypto.interfaces.DHKey;
import modules.Podminka;
import modules.Predmet;
import modules.StudijniPlan;
import modules.Uzivatel;
import sempraceidas2.mainwindow.WindowHelper;

public class PredmetyWindowController implements Initializable {

    @FXML
    private TableView<Predmet> tableView;

    @FXML
    private TableColumn<Predmet, Number> idPredmetuCol;

    @FXML
    private TableColumn<Predmet, String> zkratkaCol;

    @FXML
    private TableColumn<Predmet, String> nazevCol;
    private ObservableList<Predmet> predmetyList;
    private DatabaseHelper databaseHelper;
    @FXML
    private ImageView imagePredmety;
    @FXML
    private ListView<Podminka> podminkyList;

    @FXML
    private ListView<Uzivatel> listVyucujici;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            imagePredmety.setImage(new Image("file:src/images/book.png"));

            databaseHelper = DatabaseHelper.getInstance();
            predmetyList = FXCollections.observableArrayList();
            predmetyList.setAll(databaseHelper.getPredmety());
            tableView.setItems(predmetyList);
            initCol();
            nazevCol.setCellFactory(TextFieldTableCell.forTableColumn());

            zkratkaCol.setCellFactory(TextFieldTableCell.forTableColumn());
            idPredmetuCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
            nazevCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Predmet, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Predmet, String> event) {
                    String tmp = event.getNewValue();
                    Predmet predmet = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        predmet.setNazev(new SimpleStringProperty(tmp));
                        databaseHelper.changeNazevPredmetu(predmet.getIdPredmetu(), tmp);
                    }

                }
            });
            zkratkaCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Predmet, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Predmet, String> event) {
                    String tmp = event.getNewValue();
                    Predmet predmet = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        predmet.setZkratka(new SimpleStringProperty(tmp));
                        databaseHelper.changeZkratkaPredmetu(predmet.getIdPredmetu(), tmp);
                    }
                }
            });

            tableView.setEditable(true);
            tableView.setOnMouseClicked(e -> {
                Predmet predmet = tableView.getSelectionModel().getSelectedItem();
                if (predmet != null) {
                    listVyucujici.getItems().clear();
                    ArrayList<Uzivatel> vuycujici = databaseHelper.getVycujiciPredme(predmet.getIdPredmetu());
                    if (!vuycujici.isEmpty()) {
                        for (Uzivatel uzivatel : vuycujici) {
                            listVyucujici.getItems().add(uzivatel);

                        }

                    }
                }
            });

            tableView.setOnMouseClicked(event -> {
                Predmet predmet = tableView.getSelectionModel().getSelectedItem();
                if (predmet != null) {
                    ArrayList<Podminka> podminky = databaseHelper.getPodminkyPredmetu(predmet.getIdPredmetu());
                    if (!podminkyList.getItems().isEmpty()) {
                        podminkyList.getItems().clear();

                    }
                    if (!podminky.isEmpty()) {
                        podminkyList.getItems().addAll(podminky);

                    }
                }

            });
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void initCol() {
        idPredmetuCol.setCellValueFactory(new PropertyValueFactory<>("idPredmetu"));
        nazevCol.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        zkratkaCol.setCellValueFactory(new PropertyValueFactory<>("zkratka"));
    }

    @FXML
    private void handleButtonBackAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);

        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleButtonOdstranitAction(ActionEvent event) {
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();
        if (predmet != null) {
            databaseHelper.odstranitPredmet(predmet.getIdPredmetu());
            tableView.getItems().remove(predmet);
        }

    }

    @FXML
    private void handleButtonPridatAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        VBox box = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        hbox3.setSpacing(15);
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        box.setSpacing(10);
        Label nazevLabel = new Label("Nazev: ");
        Label zkrLabel = new Label("Zkratka: ");
        Label planLabel = new Label("StudijniPlan; ");
        ComboBox<StudijniPlan> planyBox = new ComboBox<>();
        planyBox.getItems().addAll(databaseHelper.getAllPlans());
        TextField nazevField = new TextField();
        TextField zkrField = new TextField();
        alert.setHeaderText("Pridat role");
        hbox1.getChildren().addAll(nazevLabel, nazevField);
        hbox2.getChildren().addAll(zkrLabel, zkrField);
        hbox3.getChildren().addAll(planLabel, planyBox);
        box.getChildren().addAll(hbox1, hbox2, hbox3);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK && planyBox.getValue() != null && nazevField.getText().length() > 0) {
            int id = databaseHelper.getID("PREDMETY_SQ");
            Predmet predmet = new Predmet(id, zkrField.getText(), nazevField.getText());
            tableView.getItems().add(predmet);
            databaseHelper.pridatPredmet(predmet);
            databaseHelper.pridatPredmetySp(planyBox.getValue().getId(), id);
        }

    }

    @FXML
    void handleButtonPridatVyucujchoAction(ActionEvent event) {
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();
        if (predmet != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            VBox box = new VBox();
            HBox hbox1 = new HBox();
            HBox hbox2 = new HBox();
            hbox1.setSpacing(15);
            hbox2.setSpacing(15);
            box.setSpacing(10);
            Label nazevLabel = new Label("Vybrat vyucujiciho: ");

            ComboBox<Uzivatel> uzivateleBox = new ComboBox<>();
            uzivateleBox.getItems().addAll(databaseHelper.getNoveVuycujicicPRedmetu(predmet.getIdPredmetu()));
            hbox1.getChildren().addAll(nazevLabel);
            hbox2.getChildren().addAll(uzivateleBox);
            box.getChildren().addAll(hbox1, hbox2);
            alert.getDialogPane().setContent(box);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK && uzivateleBox.getValue() != null) {
                databaseHelper.pridatVyucujicihoPredmetu(predmet.getIdPredmetu(), uzivateleBox.getValue().getId());
                listVyucujici.getItems().add(uzivateleBox.getValue());
            }
        } else {
            WindowHelper.showAlert("Nothing to do", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void handleButtonOdebratVyucujchoAction(ActionEvent event) {
        Uzivatel uzivatel = listVyucujici.getSelectionModel().getSelectedItem();
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();

        if (uzivatel != null && predmet != null) {
            System.out.println("notNull");
            databaseHelper.odebratVyucujiciho(predmet.getIdPredmetu(), uzivatel.getId());
            listVyucujici.getItems().remove(uzivatel);
        }
    }

    @FXML
    void handleButtonZmenitPlanAction(ActionEvent event) {
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();
        if (predmet != null) {
            VBox box = new VBox();
            HBox hbox1 = new HBox();
            HBox hbox3 = new HBox();
            hbox3.setSpacing(15);
            hbox1.setSpacing(15);
            box.setSpacing(10);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Zmena planu predmetu");
            Label aktPlanLabel = new Label("Aktualni st. plan(y) ");
            ArrayList<Integer> idPlanu = databaseHelper.getPlanPedmetu(predmet.getIdPredmetu());
            ArrayList<StudijniPlan> plany = new ArrayList<>();
            for (Integer intPl : idPlanu) {
                StudijniPlan plan = databaseHelper.getPlanByID(intPl);
                if (plan != null) {
                    plany.add(plan);
                }
            }
            String tmp = "";
            if (!plany.isEmpty()) {
                for (StudijniPlan studijniPlan : plany) {
                    tmp += ", " + studijniPlan.getNazev();
                }
                tmp = tmp.replaceFirst(",", "");

            } else {
                tmp = "Predmet nema studijni plan";
            }
            TextField planField = new TextField();
            planField.setText(tmp);
            hbox1.getChildren().addAll(aktPlanLabel, planField);
            box.getChildren().addAll(hbox1, hbox3);
            alert.getDialogPane().setContent(box);
            alert.showAndWait();

        }

    }

    @FXML
    void handleButtonPridatPodminkuAction(ActionEvent event) {
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();
        if (predmet != null) {
            ChoiceDialog<Podminka> dialog = new ChoiceDialog<>();
            dialog.setTitle("Choice Dialog");
            dialog.setHeaderText("Look, a Choice Dialog");
            dialog.setContentText("Choose your letter:");
            dialog.getItems().addAll(databaseHelper.getNovePodminkyPredmetu(predmet.getIdPredmetu()));
            Optional<Podminka> result = dialog.showAndWait();
            if (result.isPresent()) {
                podminkyList.getItems().add(result.get());
                databaseHelper.pridatPodminkuPredmetu(predmet.getIdPredmetu(), result.get().getIdPodminy().get());
            }
        }

    }

    @FXML
    void handleButtonOdebratPodminkuAction(ActionEvent event) {
        Predmet predmet = tableView.getSelectionModel().getSelectedItem();
        Podminka podminka = podminkyList.getSelectionModel().getSelectedItem();
        if (predmet != null && podminka != null) {
            podminkyList.getItems().remove(podminka);
            databaseHelper.deletePodminkuPredmetu(predmet.getIdPredmetu(), podminka.getIdPodminy().get());
        }

    }

}
