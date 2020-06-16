/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.oborywindow;

import com.jfoenix.controls.JFXListView;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.converter.IntegerStringConverter;
import modules.Predmet;
import modules.StudijniObor;
import modules.StudijniPlan;
import modules.Uzivatel;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class OboryWindowController implements Initializable {

    @FXML
    private TableView<StudijniObor> tableView;

    @FXML
    private TableColumn<StudijniObor, Integer> idOboruCol;

    @FXML
    private TableColumn<StudijniObor, String> nazevCol;

    @FXML
    private TableColumn<StudijniObor, String> popisCol;

    private ObservableList<StudijniObor> oboryList;
    private DatabaseHelper databaseHelper;
    @FXML
    private ImageView imageObory;
    @FXML
    private JFXListView<StudijniPlan> planyList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {

            imageObory.setImage(new Image("file:src/images/books.png"));
            tableView.setEditable(true);
            databaseHelper = DatabaseHelper.getInstance();
            oboryList = FXCollections.observableArrayList();
            oboryList.setAll(databaseHelper.getObory());
            tableView.setItems(oboryList);
            initColumns();
            nazevCol.setCellFactory(TextFieldTableCell.forTableColumn());
            popisCol.setCellFactory(TextFieldTableCell.forTableColumn());
            idOboruCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            nazevCol.setOnEditCancel(new EventHandler<TableColumn.CellEditEvent<StudijniObor, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniObor, String> event) {
                    String tmp = event.getNewValue();
                    StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        obor.setNazev(new SimpleStringProperty(tmp));
                        databaseHelper.changeNazevOboru(obor.getId(), tmp);
                    }
                }
            });
            popisCol.setOnEditCancel(new EventHandler<TableColumn.CellEditEvent<StudijniObor, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<StudijniObor, String> event) {
                    String tmp = event.getNewValue();
                    StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        obor.setPopis(new SimpleStringProperty(tmp));
                        databaseHelper.changeNazevOboru(obor.getId(), tmp);
                    }
                }
            });

                tableView.setOnMouseClicked(e -> {
                StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
                if (obor != null) {
                    planyList.getItems().clear();
                    ArrayList<StudijniPlan> plany = databaseHelper.getPlanyOboru(obor.getId());
                    if (!plany.isEmpty()) {
                        planyList.getItems().addAll(plany);

                    }
                }
            });
            
            
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void initColumns() {
        idOboruCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nazevCol.setCellValueFactory(new PropertyValueFactory<>("nazev"));
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
        StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
        databaseHelper.odstranitObor(obor.getId());
        tableView.getItems().remove(obor);
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
            StudijniObor obor = new StudijniObor(Integer.valueOf(idField.getText()), nazevField.getText(), popisField.getText());
            databaseHelper.pridatObor(obor);
            tableView.getItems().add(obor);
        }
        
         

    }

    @FXML
    void handleButtonOdebratPlanAction(ActionEvent event) {
  StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
        StudijniPlan plan = planyList.getSelectionModel().getSelectedItem();
        if (obor != null && plan != null) {
            databaseHelper.odstranitPlanOboru(plan.getId());
            planyList.getItems().remove(plan);
        }
    }

    @FXML
    void handleButtonPridatPlanAction(ActionEvent event) {
  StudijniObor obor = tableView.getSelectionModel().getSelectedItem();
        if(obor!=null){
            ChoiceDialog<StudijniPlan> choice = new ChoiceDialog<>();
            choice.setHeaderText("Select predmet");
            choice.setTitle("Predmet");
            choice.getItems().addAll( databaseHelper.getNovePlanyOboru(obor.getId()));
            Optional<StudijniPlan> result = choice.showAndWait();
            if (result.isPresent()) {
               databaseHelper.pridatPlan(choice.getSelectedItem());
               planyList.getItems().add(choice.getSelectedItem());
            }
           
            
        }
    }

}
