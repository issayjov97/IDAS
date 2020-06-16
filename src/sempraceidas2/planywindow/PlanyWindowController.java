/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.planywindow;

import com.jfoenix.controls.JFXListView;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import modules.Predmet;
import modules.StudijniPlan;
import modules.Uzivatel;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class PlanyWindowController implements Initializable {

    @FXML
    private TableView<StudijniPlan> tableView;

    @FXML
    private TableColumn<StudijniPlan, Integer> planCol;

    @FXML
    private TableColumn<StudijniPlan, String> nazevCol;

    @FXML
    private TableColumn<StudijniPlan, Integer> oborCol;

    @FXML
    private TableColumn<StudijniPlan, String> popisCol;

    private ObservableList<StudijniPlan> planyList;
    private DatabaseHelper databaseHelper;

    @FXML
    private ImageView imagePlany;
    
    @FXML
    private JFXListView<Predmet> predmetyList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            imagePlany.setImage(new Image("file:src/images/organize.png"));

            databaseHelper = DatabaseHelper.getInstance();
            planyList = FXCollections.observableArrayList();
            planyList.setAll(databaseHelper.getAllPlans());
          
            tableView.setItems(planyList);
            initCol();
            nazevCol.setCellFactory(TextFieldTableCell.forTableColumn());
            popisCol.setCellFactory(TextFieldTableCell.forTableColumn());
            planCol.setCellFactory(
                    TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            oborCol.setCellFactory(
                    TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            nazevCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudijniPlan, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniPlan, String> event) {
                    String tmp = event.getNewValue();
                    StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        plan.setNazev(new SimpleStringProperty(tmp));
                        boolean ok = databaseHelper.changeNazevPlanu(plan.getId(), tmp);
                        if (ok) {
                            WindowHelper.showAlert("Nazev planu byl zmenen", Alert.AlertType.INFORMATION);
                        }

                    }
                }
            });
            popisCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudijniPlan, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniPlan, String> event) {

                    String tmp = event.getNewValue();
                    StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        plan.setPopis(new SimpleStringProperty(tmp));
                        boolean ok = databaseHelper.changePopisPlanu(plan.getId(), tmp);
                        if (ok) {
                            WindowHelper.showAlert("Popis planu byl zmenen", Alert.AlertType.INFORMATION);
                        }

                    }
                }
            });
            oborCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudijniPlan, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniPlan, Integer> event) {
                    int tmp = event.getNewValue();
                    StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != 0) {
                        plan.setIdOboru(new SimpleIntegerProperty(tmp));
                        boolean ok = databaseHelper.changeIdOboruPlan(plan.getId(), tmp);
                        if (ok) {
                            WindowHelper.showAlert("Id oboru byl zmenen", Alert.AlertType.INFORMATION);
                        }

                    }
                }
            });
            planCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudijniPlan, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniPlan, Integer> event) {
                    int tmp = event.getNewValue();
                    StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != 0) {
                        plan.setId(new SimpleIntegerProperty(tmp));
                        boolean ok = databaseHelper.changeIdOboruPlan(plan.getId(), tmp);
                        if (ok) {
                            WindowHelper.showAlert("Id planu byl zmenen", Alert.AlertType.INFORMATION);
                        }
                    }
                }
            });
            tableView.setEditable(true);
                        tableView.setOnMouseClicked(e -> {
                StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
                if (plan != null) {
                    predmetyList.getItems().clear();
                    ArrayList<Predmet> predmety = databaseHelper.getPRedmetyPlanu(plan.getId());
                    if (!predmety.isEmpty()) {
                        for (Predmet predmet : predmety) {
                            predmetyList.getItems().add(predmet);

                        }

                    }
                }
            });
            
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
        
     
    }

    private void initCol() {
        planCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nazevCol.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        oborCol.setCellValueFactory(new PropertyValueFactory<>("idOboru"));
        popisCol.setCellValueFactory(new PropertyValueFactory<>("popis"));

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
        StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
        databaseHelper.odstranitPlan(plan.getId());
        tableView.getItems().remove(plan);
    }

    @FXML
    private void handleButtonPridatAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        VBox box = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox4 = new HBox();
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        hbox4.setSpacing(15);
        box.setSpacing(10);
        Label idLabel = new Label("Id oboru: ");
        Label nazevLabel = new Label("Nazev: ");
        Label popisLabel = new Label("Popis: ");
        TextField nazevField = new TextField();
        TextField popisField = new TextField();
        TextField idField = new TextField();
        alert.setHeaderText("Pridat role");
        hbox4.getChildren().addAll(idLabel, idField);
        hbox1.getChildren().addAll(nazevLabel, nazevField);
        hbox2.getChildren().addAll(popisLabel, popisField);
        box.getChildren().addAll(hbox4, hbox1, hbox2);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            int id = databaseHelper.getID("ST_PLANY_SQ");
            StudijniPlan plan = new StudijniPlan(id, nazevField.getText(), Integer.valueOf(idField.getText()), popisField.getText());
            databaseHelper.pridatPlan(plan);
            tableView.getItems().add(plan);
        }

    }
    @FXML
    void handleButtonOdebratPredmetAction(ActionEvent event) {
    Predmet predmet= predmetyList.getSelectionModel().getSelectedItem();
        StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
        if (predmet != null && plan != null) {
            databaseHelper.odebratPredmetZPlanu(plan.getId(), predmet.getIdPredmetu());
            predmetyList.getItems().remove(predmet);
        }
    }
   
    @FXML
    void handleButtonPridatPredmetAction(ActionEvent event) {
         StudijniPlan plan = tableView.getSelectionModel().getSelectedItem();
        if(plan!=null){
            ChoiceDialog<Predmet> choice = new ChoiceDialog<>();
            choice.setHeaderText("Select predmet");
            choice.setTitle("Predmet");
            choice.getItems().addAll( databaseHelper.getPredmetyPlan(plan.getId()));
            Optional<Predmet> result = choice.showAndWait();
            if (result.isPresent()) {
               databaseHelper.pridatPredmetySp(plan.getId(), choice.getSelectedItem().getIdPredmetu());
               predmetyList.getItems().add(choice.getSelectedItem());
            }
           
            
        }

    }



}
