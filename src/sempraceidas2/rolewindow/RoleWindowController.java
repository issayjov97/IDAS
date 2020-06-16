/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.rolewindow;


import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import modules.Role;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class RoleWindowController implements Initializable {

    @FXML
    private TableView<Role> tableView;

    @FXML
    private TableColumn<Role, Integer> idRoleCol;

    @FXML
    private TableColumn<Role, String> nazevCol;

    @FXML
    private TableColumn<Role, String> poznamkaCol;

    @FXML
    private TableColumn<Role, String> opravneniCol;

    private ObservableList<Role> roleList;
    private DatabaseHelper databaseHelper;
    @FXML
    private ImageView imagView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            imagView.setImage(new Image("file:src/images/board-games-with-roles.png"));

            databaseHelper = DatabaseHelper.getInstance();
            roleList = FXCollections.observableArrayList();
            roleList.setAll(databaseHelper.getAllRoles());
            tableView.setItems(roleList);
            initCol();
            nazevCol.setCellFactory(TextFieldTableCell.forTableColumn());
            opravneniCol.setCellFactory(TextFieldTableCell.forTableColumn());
            poznamkaCol.setCellFactory(TextFieldTableCell.forTableColumn());
            idRoleCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            nazevCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Role, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Role, String> event) {
                    String tmp = event.getNewValue();
                    Role role = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        role.setNazev(new SimpleStringProperty(tmp));
                        databaseHelper.changeNazevRole(role.getIdRole(), tmp);
                    }
                }
            });
            poznamkaCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Role, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Role, String> event) {
                    String tmp = event.getNewValue();
                    Role role = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        role.setPoznamka(new SimpleStringProperty(tmp));
                        databaseHelper.changePoznamkaRole(role.getIdRole(), tmp);
                    }
                }
            });
            opravneniCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Role, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Role, String> event) {
                    String tmp = event.getNewValue();
                    Role role = tableView.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        role.setOpravneni(new SimpleStringProperty(tmp));
                        databaseHelper.changeOpravneniRole(role.getIdRole(), tmp);
                    }
                }
            });

            tableView.setEditable(true);
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void initCol() {
        idRoleCol.setCellValueFactory(new PropertyValueFactory<>("idRole"));
        nazevCol.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        poznamkaCol.setCellValueFactory(new PropertyValueFactory<>("poznamka"));
        opravneniCol.setCellValueFactory(new PropertyValueFactory<>("opravneni"));

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
        Role role = tableView.getSelectionModel().getSelectedItem();
        boolean ok = databaseHelper.odstranitRole(role.getIdRole());
        if (ok) {
            roleList.remove(role);
            WindowHelper.showAlert("Role byla odstranena", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleButtonPridatAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        VBox box = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        hbox3.setSpacing(15);
        hbox4.setSpacing(15);
        box.setSpacing(10);
        Label idLabel = new Label("ID role: ");
        Label nazevLabel = new Label("Nazev: ");
        Label pozLabel = new Label("Poznamka: ");
        Label opravLabel = new Label("Opravneni:");
        TextField nazevField = new TextField();
        TextField poznamField = new TextField();
        TextField opravField = new TextField();
        TextField idField = new TextField();
        alert.setHeaderText("Pridat role");
        hbox4.getChildren().addAll(idLabel, idField);
        hbox1.getChildren().addAll(nazevLabel, nazevField);
        hbox2.getChildren().addAll(pozLabel, poznamField);
        hbox3.getChildren().addAll(opravLabel, opravField);
        box.getChildren().addAll(hbox4, hbox1, hbox2, hbox3);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Role role = new Role(Integer.valueOf(idField.getText()), nazevField.getText(), poznamField.getText(), opravField.getText());
            boolean ok = databaseHelper.pridatRole(role);
            if (ok) {
                roleList.add(role);
                WindowHelper.showAlert("Role byla pridana", Alert.AlertType.INFORMATION);
            }

        }

    }

}
