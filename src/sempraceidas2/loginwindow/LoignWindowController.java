/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.loginwindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import database.DatabaseHelper;
import database.PasswordGenerator;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import modules.Predmet;
import modules.Role;
import modules.SessionUser;
import modules.Skupina;
import modules.StudijniObor;
import modules.StudijniPlan;
import modules.Uzivatel;

import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class LoignWindowController implements Initializable {

    @FXML
    private JFXButton loginBut;
    @FXML
    private JFXButton RegBut;
    @FXML
    private JFXTextField loginField;
    @FXML
    private JFXPasswordField passwordField;
    private DatabaseHelper databaseHelper;
    private final String regex = "^\\s*$";

    @FXML
    private ImageView Image1;

    private JFXButton nastaveniBtn;
    @FXML
    private ImageView image2;

    @FXML
    private ImageView mainImage;

    @FXML
    private Hyperlink linkAboutUs;

    @FXML
    private ImageView aboutUsImage;

    @FXML
    private Hyperlink facebookLink;

    @FXML
    private ImageView facebookImage;

    @FXML
    private StackPane stck;
    @FXML
    private Hyperlink twitterLink;

    @FXML
    private ImageView twitterImage;

    @FXML
    private BorderPane borderPane;
    private ArrayList<Role> roleUziv;
    private ArrayList<StudijniPlan> planyUziv;

    @FXML
    private Hyperlink linkedIn;
    @FXML
    private ImageView linkedInImage;
    @FXML
    private MenuBar menuBar;
    @FXML
    private JFXListView<StudijniPlan> planyList;

    @FXML
    private JFXListView<Predmet> predmetyList;

    @FXML
    private VBox planyPane;
    @FXML
    private Pane pane2;

    @FXML
    private VBox oboryPane;

    @FXML
    private JFXListView<StudijniObor> oboryList;

    @FXML
    private JFXListView<StudijniPlan> planyList2;

    @FXML
    private VBox skupinyPane;
    @FXML
    private JFXListView<Predmet> skupinyList;
    @FXML
    private JFXListView<Uzivatel> vuycList;

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
            planyPane.setVisible(false);
            oboryPane.setVisible(false);
            skupinyPane.setVisible(false);
            planyList.getItems().addAll(databaseHelper.getAllPlans());
            oboryList.getItems().addAll(databaseHelper.getObory());
            skupinyList.getItems().addAll(databaseHelper.getPredmety());
            Image1.setImage(new Image("file:src/images/avatar.png"));
            image2.setImage(new Image("file:src/images/key_1.png"));
            aboutUsImage.setImage(new Image("file:src/images/students-cap.png"));
            facebookImage.setImage(new Image("file:src/images/facebook.png"));
            twitterImage.setImage(new Image("file:src/images/twitter.png"));
            mainImage.setImage(new Image("file:src/images/upceMain.png"));
            linkedInImage.setImage(new Image("file:src/images/linkedin.png"));
            Image image = new Image("file:src/images/mainBackGround.jpg");
            facebookLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://www.facebook.com/univerzita.pardubice"));
                    } catch (IOException | URISyntaxException ex) {
                        Logger.getLogger(LoignWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            twitterLink.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://twitter.com/unipardubice"));
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(LoignWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            linkedIn.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.linkedin.com/school/university-of-pardubice/"));
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(LoignWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            planyList.setOnMouseClicked(e -> {
                StudijniPlan plan = planyList.getSelectionModel().getSelectedItem();
                if (plan != null) {
                    if (!predmetyList.getItems().isEmpty()) {
                        predmetyList.getItems().clear();
                    }
                    ArrayList<Predmet> predmety = databaseHelper.getPRedmetyPlanu(plan.getId());
                    if (!predmety.isEmpty()) {
                        predmetyList.getItems().addAll(predmety);
                    }
                }
            });

            oboryList.setOnMouseClicked(e -> {
                StudijniObor obor = oboryList.getSelectionModel().getSelectedItem();
                if (obor != null) {

                    if (!planyList2.getItems().isEmpty()) {
                        planyList2.getItems().clear();
                    }
                    ArrayList<StudijniPlan> plany = databaseHelper.getPlanyOboru(obor.getId());

                    if (!plany.isEmpty()) {
                        planyList2.getItems().addAll(plany);
                    }

                }
            });
            skupinyList.setOnMouseClicked(e -> {
                Predmet predmet = skupinyList.getSelectionModel().getSelectedItem();
                if (predmet != null) {
                    if (!vuycList.getItems().isEmpty()) {
                        vuycList.getItems().clear();
                    }
                    ArrayList<Uzivatel> uzivatele = databaseHelper.getVycujiciPredme(predmet.getIdPredmetu());
                    if (!uzivatele.isEmpty()) {
                        vuycList.getItems().addAll(uzivatele);
                    }

                }
            });
        } catch (SQLException exception) {
            WindowHelper.showAlert(exception.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String userLogin = loginField.getText();
        String userHeslo = passwordField.getText();
        int blokace;

        int result = databaseHelper.callUserLoginProcedure(userLogin, userHeslo);
        if (result > 0) {
            blokace = databaseHelper.callFunctionIsUserBlocked(result);

            if (blokace == 0) {
                WindowHelper.showAlert("You are blocked!", Alert.AlertType.INFORMATION);
            } else {
                try {
                    Uzivatel uzivatel = databaseHelper.getUser(userLogin, userHeslo);
                    System.out.println(uzivatel);
                    SessionUser.setUzivatel(uzivatel);
                    System.out.println("Session");
                    System.out.println(SessionUser.getUzivatelOnline());
                    WindowHelper.closeWindow(event);
                    WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(LoignWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            WindowHelper.showAlert("Nespravny login nebo heslo, zkuste znovu", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void handleRegistraceButtonAction(ActionEvent event) {
        planyUziv = new ArrayList<>();
        roleUziv = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        VBox box = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        HBox hbox6 = new HBox();
        HBox hbox7 = new HBox();
        HBox hbox8 = new HBox();
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        hbox3.setSpacing(15);
        hbox4.setSpacing(15);
        hbox5.setSpacing(15);
        hbox6.setSpacing(15);
        hbox7.setSpacing(15);
        hbox8.setSpacing(15);
        box.setSpacing(10);
        box.setPrefWidth(450);
        box.setPrefHeight(300);

        Label jmenoLabel = new Label("Jmeno");
        Label prijmeniLabel = new Label("Prijmeni");
        Label hesloiLabel = new Label("Heslo");
        Label emailLabel = new Label("Email");
        Label loginLabel = new Label("Login");
        Label poznamkalLabel = new Label("Poznamky");
        Label roleLabel = new Label("Role");
        Label planLabel = new Label("Studijni plan");

        TextField jmenoField = new TextField();
        TextField prijmeniField = new TextField();
        TextField hesloField = new TextField();
        TextField emailField = new TextField();
        TextField loginField = new TextField();
        TextField poznamkyField = new TextField();
        ComboBox<Role> roleBox = new ComboBox();
        ArrayList<Role> roles = databaseHelper.getAllRoles();
        ArrayList<StudijniPlan> planes = databaseHelper.getAllPlans();
        roleBox.getItems().addAll(roles);
        ComboBox<StudijniPlan> planyBox = new ComboBox();
        planyBox.getItems().addAll(planes);
        planyBox.getItems().addAll(planyUziv);
        planyBox.valueProperty().addListener(new ChangeListener<StudijniPlan>() {

            @Override
            public void changed(ObservableValue<? extends StudijniPlan> observable, StudijniPlan oldValue, StudijniPlan newValue) {
                if (newValue.getNazev().length() > 0 && checkAddPlan(newValue.getNazev())) {

                    planyUziv.add(newValue);

                } else {
                    WindowHelper.showAlert("Plan uz byl pridan", Alert.AlertType.INFORMATION);
                }
            }
        }
        );

        roleBox.valueProperty()
                .addListener(new ChangeListener<Role>() {
                    @Override
                    public void changed(ObservableValue<? extends Role> observable, Role oldValue, Role newValue) {
                        if (checkAddRole(newValue.getNazev())) {

                            roleUziv.add(newValue);
                            planyBox.setVisible(true);
                            planyBox.setDisable(false);
                            planLabel.setVisible(true);
                            if (newValue.getIdRole() == 66) {
                                planyBox.setVisible(false);
                                planyBox.setDisable(true);
                                planLabel.setVisible(false);
                            }
                        } else {
                            WindowHelper.showAlert("Role uz byla pridana", Alert.AlertType.INFORMATION);
                        }

                    }
                });

        alert.setResizable(
                true);
        alert.getDialogPane()
                .setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(
                "Nastaveni");
        hbox1.getChildren()
                .addAll(jmenoLabel, jmenoField);
        hbox2.getChildren()
                .addAll(prijmeniLabel, prijmeniField);
        hbox3.getChildren()
                .addAll(hesloiLabel, hesloField);
        hbox4.getChildren()
                .addAll(emailLabel, emailField);
        hbox5.getChildren()
                .addAll(loginLabel, loginField);
        hbox8.getChildren()
                .addAll(roleLabel, roleBox);
        hbox6.getChildren()
                .addAll(planLabel, planyBox);
        box.getChildren()
                .addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox8, hbox7, hbox6);
        alert.getDialogPane()
                .setContent(box);
        Optional<ButtonType> result = alert.showAndWait();
        String valid = isValid(jmenoField.getText(), prijmeniField.getText(), hesloField.getText(), loginField.getText(), emailField.getText());
        if (result.get() == ButtonType.OK && !planyUziv.isEmpty() && !roleUziv.isEmpty()) {
            int idUziv = databaseHelper.callFunctionAddUpdateUser(loginField.getText(), hesloField.getText(), emailField.getText(), jmenoField.getText(), prijmeniField.getText());
            if (idUziv > 0) {

                Uzivatel uzivatel = new Uzivatel(idUziv, loginField.getText(), hesloField.getText(), jmenoField.getText(),
                        prijmeniField.getText(), poznamkyField.getText(), emailField.getText(), 1);
                SessionUser.setUzivatel(uzivatel);

                databaseHelper.addPlan(planyUziv, uzivatel.getId());
                roleUziv.forEach((role) -> {
                    boolean a = databaseHelper.pridatRole(uzivatel.getId(), role.getIdRole());
                });
            }

        } else {
            WindowHelper.showAlert(valid, Alert.AlertType.WARNING);
        }
    }

    private String isValid(String name, String surname, String password, String login, String email) {
        String tmp = "";
        if (!surname.matches("[A-Z][a-z]*")) {
            tmp += ",Wrong surname";
        }
        if (!name.matches("^[\\p{L} .'-]+$")) {
            tmp += " ,Wrong name";
        }
        if (!login.matches("^[a-zA-Z0-9._-]{3,}$")) {
            tmp += " ,Wrong username";
        }
        if (!password.matches("^[a-zA-Z0-9._-]{3,}$")) {
            tmp += " ,Wrong password";
        }
        if (!email.matches("^(.+)@(.+)$")) {
            tmp += " ,Wrong email";
        }

        tmp = tmp.replaceFirst(",", "");
        return tmp;
    }

    void handleCloseButtonAction(ActionEvent event) {
        stck.getChildren().set(0, new Button("Heloo"));
    }

    boolean checkAddRole(String value) {
        if (roleUziv.isEmpty()) {
            return true;
        } else {
            for (Role role : roleUziv) {
                if (role.getNazev().equals(value)) {

                    return false;
                }
            }
        }
        return true;

    }

    private boolean checkAddPlan(String nazev) {
        if (planyUziv.isEmpty()) {
            return true;
        } else {
            for (StudijniPlan studijniPlan : planyUziv) {
                if (studijniPlan.getNazev().equals(nazev)) {
                    return false;
                }
            }
        }
        return true;

    }

    @FXML
    void handleMenuItemHlavniStrankaAction(ActionEvent event) {
        stck.setVisible(true);
        oboryPane.setVisible(false);
        planyPane.setVisible(false);
        skupinyPane.setVisible(false);

    }

    @FXML
    void handleMenuItemOboryAction(ActionEvent event) {
        stck.setVisible(false);
        planyPane.setVisible(false);
        oboryPane.setVisible(true);
        skupinyPane.setVisible(false);

    }

    @FXML
    void handleMenuItemPlanyPredmetyAction(ActionEvent event) {
        stck.setVisible(false);
        oboryPane.setVisible(false);
        planyPane.setVisible(true);
        skupinyPane.setVisible(false);

    }

    @FXML
    void handleMenuItemSkupinyAction(ActionEvent event) {
        skupinyPane.setVisible(true);
        stck.setVisible(false);
        oboryPane.setVisible(false);
        planyPane.setVisible(false);

    }
}
