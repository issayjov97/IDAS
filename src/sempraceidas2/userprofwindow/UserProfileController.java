/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.userprofwindow;

import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import modules.Obrazek;
import modules.Role;
import modules.StudijniObor;
import modules.StudijniPlan;
import modules.Uzivatel;
import sempraceidas2.contactswindow.ContactsWindowController;
import sempraceidas2.mainwindow.WindowHelper;
import sempraceidas2.obrazkywindow.ObrazkyWindowController;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class UserProfileController implements Initializable {

    @FXML
    private ImageView image1;

    @FXML
    private Circle circle;

    @FXML
    private Label jmenoLabel;

    @FXML
    private Label prijmeniLabel;

    @FXML
    private VBox mainBox;

    @FXML
    private ImageView imageObrazky;

    @FXML
    private Label planyLabel;

    @FXML
    private Label oboryLabel;

    @FXML
    private Label roleLabel;
    private Uzivatel uzivatel;
    private ArrayList<StudijniPlan> plany;
    private ArrayList<StudijniObor> obory;
    private ArrayList<Role> role;
    private DatabaseHelper dh;

    @FXML
    void handleButtonKontaktyAction(ActionEvent event) {
     try {
            WindowHelper.closeWindow(event);
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/contactswindow/ContactsWindow.fxml"), "Slider", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleButtonObrazkyAction(ActionEvent event) {
        try {
            WindowHelper.closeWindow(event);
            ObrazkyWindowController.setIdUzivatele(uzivatel.getId());
            WindowHelper.loadWindow(getClass().getResource("/sempraceidas2/obrazkywindow/ObrazkyWindow.fxml"), "Slider", null);
        } catch (IOException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
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
            oboryLabel.setVisible(true);
            planyLabel.setVisible(true);
        }
        roleStr = role.stream().map((roleUz) -> roleUz.getNazev() + " ").reduce(roleStr, String::concat);
        roleLabel.setText(roleStr);
        planyLabel.setText(planyStr);
        oboryLabel.setText(oboryStr);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dh = DatabaseHelper.getInstance();
            plany = new ArrayList<>();
            obory = new ArrayList<>();
            role = new ArrayList<>();
            uzivatel = ContactsWindowController.getUzivatel();
            if (uzivatel != null) {
                Obrazek profileimage = dh.getProfileImage(uzivatel.getId());
                if (profileimage != null) {
                    circle.setFill(new ImagePattern(profileimage.getImage()));
                } else {
                    circle.setFill(new ImagePattern(new Image("file:src/images/user.png")));
                }

                System.out.println("A:" + uzivatel.getId() + ", Jmeno:" + uzivatel.getJmeno());
                plany = dh.getPlan(uzivatel.getId());
                plany.forEach((studijniPlan) -> {
                    obory.add(dh.getOborByID(studijniPlan.getIdOboru()));
                }); 
                role = dh.getRole(uzivatel.getId());
                setData();
               
    

            }

        } catch (SQLException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
