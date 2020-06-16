/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.mainwindow;

import Files.CSVParser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import database.DBChangeNotification;
import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalDateTimeStringConverter;
import modules.ObjektOznameniZmena;
import modules.Obrazek;
import modules.Oznameni;
import modules.Role;
import modules.SessionUser;
import modules.StudijniObor;
import modules.StudijniPlan;
import modules.Udalost;
import modules.Uzivatel;

import sempraceidas2.obrazkywindow.ObrazkyWindowController;

/**
 *
 * @author dzhohar
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Circle circle;
    @FXML
    private Label jmenoLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label prijmeniLabel;
    private DatabaseHelper dh;
    @FXML
    private JFXListView<Udalost> listUdalosti;
    @FXML
    private JFXToggleButton soundBtn;

    @FXML
    private ImageView logOutBtmage;

    @FXML
    private ImageView oznameniItemImage;

    @FXML
    private ImageView imageNstavBtn;
    @FXML
    private Label planLabel;
    @FXML
    private Label oborLabel;
    private Uzivatel uzivatel;
    TextField prijmeniField;
    TextField jmenoField;
    TextField hesloField;
    TextField emailField;
    @FXML
    private JFXButton btnSimulation;
    @FXML
    private JFXButton vuycPredmetyButton;
    public static ArrayList<StudijniPlan> plany;
    private ArrayList<StudijniObor> obory;
    public static ArrayList<Role> role;
    private ArrayList<Oznameni> oznameniList;
    private static boolean isAdmin;
    private static boolean simulation;
    private static boolean isVyujuici;

    public static boolean isIsAdmin() {
        return isAdmin;
    }

    public static boolean isIsVyujuici() {
        return isVyujuici;
    }

    @FXML
    private VBox adminBox;
    @FXML
    private VBox mainBox;
    @FXML
    private ImageView image1;
    @FXML
    private ImageView imageUdal;
    @FXML
    private ImageView imageNastav;
    @FXML
    private ImageView imagePredmety;
    @FXML
    private ImageView imageObory;
    @FXML
    private ImageView imagePlany;
    @FXML
    private ImageView imageRole;
    @FXML
    private JFXButton nastaveniBtn;
    @FXML
    private ImageView imageNastav1;

    @FXML
    private BorderPane bordPn;
    @FXML
    private ImageView simulaceImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            if (simulation) {
                btnSimulation.setVisible(true);
                btnSimulation.setDisable(false);
            }
            setImages();
            vuycPredmetyButton.setVisible(false);
            vuycPredmetyButton.setDisable(true);
            obory = new ArrayList<>();
            oznameniList = new ArrayList<>();
            oborLabel.setVisible(false);
            planLabel.setVisible(false);
            adminBox.setVisible(false);
            adminBox.setDisable(true);
            listUdalosti.setVisible(false);
            listUdalosti.setDisable(true);
            emailField = new TextField();
            hesloField = new TextField();
            jmenoField = new TextField();
            prijmeniField = new TextField();
            dh = DatabaseHelper.getInstance();
            circle.setStrokeWidth(5);
            uzivatel = SessionUser.getUzivatelOnline();
            DBChangeNotification.startListener();
            DBChangeNotification dbcn = DBChangeNotification.getInstance();
            dbcn.setButton(nastaveniBtn);
            Obrazek profileimage = dh.getProfileImage(uzivatel.getId());
            if (profileimage != null) {
                circle.setFill(new ImagePattern(profileimage.getImage()));
            } else {
                circle.setFill(new ImagePattern(new Image("file:src/images/user.png")));
            }
            plany = dh.getPlan(uzivatel.getId());

            plany.forEach((studijniPlan) -> {
                obory.add(dh.getOborByID(studijniPlan.getIdOboru()));
            });
            role = dh.getRole(uzivatel.getId());
            role.forEach((roleUz) -> {
                if (roleUz.getIdRole() == 66) {
                    isAdmin = true;
                    adminBox.setVisible(true);
                    adminBox.setDisable(false);
                } else if (roleUz.getIdRole() == 77) {
                    isVyujuici = true;
                    vuycPredmetyButton.setVisible(true);
                    vuycPredmetyButton.setDisable(false);

                }
            });
            setData();
            listUdalosti.getItems().addAll(dh.getUdalost(uzivatel.getId()));
            System.out.println("");
            inDrawer();
            prijmeniField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    uzivatel.setPrijmeni(new SimpleStringProperty(newValue));
                    dh.changePrijmeni(uzivatel.getId(), newValue);
                }

            });
            jmenoField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    uzivatel.setJmeno(new SimpleStringProperty(newValue));
                    dh.changeJmeno(uzivatel.getId(), newValue);
                }

            });
            emailField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    uzivatel.setEmail(new SimpleStringProperty(newValue));
                    dh.changeEmail(uzivatel.getId(), newValue);
                }

            });
            hesloField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    uzivatel.setHeslo(new SimpleStringProperty(newValue));
                    dh.changeHeslo(uzivatel.getId(), newValue);
                }

            });
        } catch (SQLException | IOException ex) {
            ex.getCause().getMessage();
        }

    }

    public void setData() {
        jmenoLabel.setText(uzivatel.getJmeno());
        prijmeniLabel.setText(uzivatel.getPrijmeni());
        String planyStr = "";
        String oboryStr = "";
        String roleStr = "";
        planyStr = plany.stream().map((studijniPlan) -> studijniPlan.getNazev() + " ").reduce(planyStr, String::concat);
        oboryStr = obory.stream().map((studijniObor) -> studijniObor.getNazev() + " ").reduce(oboryStr, String::concat);
        if (!plany.isEmpty()) {
            oborLabel.setVisible(true);
            planLabel.setVisible(true);
        }
        roleStr = role.stream().map((roleUz) -> roleUz.getNazev() + " ").reduce(roleStr, String::concat);
        roleLabel.setText(roleStr);
        planLabel.setText(planyStr);
        oborLabel.setText(oboryStr);
    }

    private void inDrawer() throws IOException {
        AnchorPane toolbar = FXMLLoader.load(getClass().getResource("Drawer.fxml"));
        drawer.setSidePane(toolbar);
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);

        task.setRate(-1);
        hamburger.setOnMouseClicked(e -> {
            task.setRate(task.getRate() * -1);
            task.play();
            if (drawer.isClosed()) {
                drawer.open();
                drawer.setMinWidth(200);
            } else {
                drawer.close();
            }
        });

    }

    @FXML
    void handleButtonUdalostiAction(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            listUdalosti.setVisible(true);
            listUdalosti.setDisable(false);

        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            listUdalosti.setVisible(false);
            listUdalosti.setDisable(true);
        }
    }

    @FXML
    void handleButtonOdebratAction(ActionEvent event) {
        Udalost udalost = listUdalosti.getSelectionModel().getSelectedItem();
        if (udalost != null) {
            dh.odebratUdalost(udalost.getIdUziv(), udalost.getIdUdal());
            listUdalosti.getItems().remove(udalost);
        }

    }

    @FXML
    private void handleButtonNastaveniAction(ActionEvent event) {
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
        Label jmenoLab = new Label("Jmeno");
        Label prijmeniLab = new Label("Prijmeni");
        Label hesloiLabel = new Label("Heslo");
        Label emailLabel = new Label("Email");
        jmenoField.setText(uzivatel.getJmeno());
        prijmeniField.setText(uzivatel.getPrijmeni());
        hesloField.setText(uzivatel.getHeslo());
        emailField.setText(uzivatel.getEmail());
        alert.setHeaderText("Nastaveni");
        hbox1.getChildren().addAll(jmenoLab, jmenoField);
        hbox2.getChildren().addAll(prijmeniLab, prijmeniField);
        hbox3.getChildren().addAll(hesloiLabel, hesloField);
        hbox4.getChildren().addAll(emailLabel, emailField);
        box.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            setData();
        }

    }

    @FXML
    private void handleButtonLogOutAction(ActionEvent event) {
        try {
            clearData();
            SessionUser.unsetUzivatel();
            Stage stage = (Stage) bordPn.getScene().getWindow();
            stage.close();
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/loginwindow/LoignWindow.fxml"), "MainWindow", null);

        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void handleButtonPredmetyAction(ActionEvent event) {

        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/predmetywindow/PredmetyWindow.fxml"), "Predmety", null);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleButtonOboryAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/oborywindow/OboryWindow.fxml"), "Studijni obory", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void handleButtonPlanyAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/planywindow/PlanyWindow.fxml"), "Studijni plany", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleButtonRoleAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/rolewindow/RoleWindow.fxml"), "Role", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
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
        HBox hbox5 = new HBox();
        hbox1.setSpacing(15);
        hbox2.setSpacing(15);
        hbox3.setSpacing(15);
        hbox4.setSpacing(15);
        hbox5.setSpacing(15);
        box.setSpacing(10);
        Label datLabel = new Label("Datum: ");
        Label casLabel = new Label("Cas: ");
        JFXTimePicker jFXTimePicker = new JFXTimePicker();
        Label obsahLabel = new Label("Obsah:");
        Label nazevLabel = new Label("Nazev:");
        Label mistoLabel = new Label("Misto knani:");
        JFXDatePicker jFXDatePicker = new JFXDatePicker();

        TextField obsahField = new TextField();
        TextField nazevField = new TextField();
        TextField mistoField = new TextField();
        alert.setHeaderText("Nastaveni");
        hbox1.getChildren().addAll(datLabel, jFXDatePicker);
        hbox2.getChildren().addAll(obsahLabel, obsahField);
        hbox3.getChildren().addAll(nazevLabel, nazevField);
        hbox4.getChildren().addAll(mistoLabel, mistoField);
        hbox5.getChildren().addAll(casLabel, jFXTimePicker);
        box.getChildren().addAll(hbox1, hbox5, hbox2, hbox3, hbox4);
        alert.getDialogPane().setContent(box);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                LocalDateTime ldt = LocalDateTime.of(jFXDatePicker.getValue(), jFXTimePicker.getValue());
                Timestamp timestamp = Timestamp.valueOf(ldt);
                Udalost udalost = new Udalost(0, timestamp, obsahField.getText(), nazevField.getText(), uzivatel.getId(), mistoField.getText());
                dh.pridatUdalost(udalost);
                listUdalosti.getItems().clear();
                listUdalosti.getItems().addAll(dh.getUdalost(uzivatel.getId()));
            } catch (SQLException ex) {
                WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            }

        }
    }

    @FXML
    public void handleButtonObrazkyAction(ActionEvent event) {
        try {

            WindowHelper.closeWindow(event);
            ObrazkyWindowController.setIdUzivatele(SessionUser.getUzivatelOnline().getId());
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/obrazkywindow/ObrazkyWindow.fxml"), "Slider", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleMenuItem3ZobrazitOznameniAction(ActionEvent event) {
        ArrayList<ObjektOznameniZmena> list = DBChangeNotification.getNotifications();
        String alertMessage = null;
        String[] tmp = null;
        if (!list.isEmpty()) {

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                JFXListView<ObjektOznameniZmena> listView = new JFXListView<>();
                listView.setMinWidth(400);

                listView.setCellFactory(new ListCellFactory());
                listView.getItems().addAll(list);
                alert.getDialogPane().setContent(listView);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    list.stream().filter(item -> !item.getNotificationType().equals("FriendRequest")).forEach((objektOznameniZmena) -> {
                        dh.updateNotifications(objektOznameniZmena.getIdObjectNatification());
                    });
                }
            } catch (SQLException ex) {
                WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            }
        } else {
            WindowHelper.showAlert("You have no notifications", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void handleMouseClickedAction(MouseEvent event) {
        try {
            ObrazkyWindowController.setIdUzivatele(SessionUser.getUzivatelOnline().getId());
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/obrazkywindow/ObrazkyWindow.fxml"), "Slider", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleButtonVuycPredmetyAction(ActionEvent event) {

        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/vyucpredmetywindow/PredmetyVyucWindow.fxml"), "Slider", null);
        } catch (IOException ex) {
              WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    private void setImages() {
        image1.setImage(new Image("file:src/images/upceMain.png"));
        imageUdal.setImage(new Image("file:src/images/calendar.png"));
        imageNastav.setImage(new Image("file:src/images/customer-support.png"));
        imagePredmety.setImage(new Image("file:src/images/book.png"));
        imagePlany.setImage(new Image("file:src/images/organize.png"));
        imageObory.setImage(new Image("file:src/images/books.png"));
        imageRole.setImage(new Image("file:src/images/board-games-with-roles.png"));
        imageNstavBtn.setImage(new Image("file:src/images/settings-gears.png"));
        logOutBtmage.setImage(new Image("file:src/images/logout.png"));
        oznameniItemImage.setImage(new Image("file:src/images/notification.png"));
    }

    @FXML
    void handleMenuItemSimulationAction(ActionEvent event) {
        ChoiceDialog<Uzivatel> dialog = new ChoiceDialog<>();
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your letter:");
        dialog.getItems().addAll(dh.getAllUsers());
        Optional<Uzivatel> result = dialog.showAndWait();

        if (result.isPresent()) {

            try {
                simulation = true;
                clearData();
                Uzivatel uzivatelSim = dialog.getResult();
                btnSimulation.setVisible(true);
                btnSimulation.setDisable(false);

                SessionUser.setUzivatel(uzivatelSim);
                Stage stage = (Stage) bordPn.getScene().getWindow();
                stage.close();
                WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);
            } catch (IOException ex) {
 WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);            }

        }

    }

    private void clearData() {
        plany.clear();
        role.clear();
        obory.clear();
        isAdmin = false;
        isVyujuici = false;

    }

    @FXML
    void handleButtonSimulationAction(ActionEvent event) {
        try {
            btnSimulation.setVisible(false);
            btnSimulation.setDisable(true);
            simulation = false;
            clearData();
            Uzivatel uz = SessionUser.getUzivatelReal();
            SessionUser.setUzivatel(uz);
            Stage stage = (Stage) bordPn.getScene().getWindow();
            stage.close();
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/mainwindow/FXMLDocument.fxml"), "MainWindow", null);
        } catch (IOException ex) {
             WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void handleButtonImportAction(ActionEvent event) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose");
        dialog.getItems().add("Predmety");
        dialog.getItems().add("Vyucujici");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
            if (result.get().equals("Predmety")) {
                CSVParser.importPredmety();
            } else {
                    CSVParser.importUcitele();
                } }catch (IOException ex) {
                      WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
                }

            }
    }

    @FXML
    void handleButtonPodminkyAction(ActionEvent event) {

        try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/podminkywindow/PodminkyWindow.fxml"), "Podminky", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
