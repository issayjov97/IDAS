/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.obrazkywindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import database.DatabaseHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.lang.model.element.Element;
import modules.Komentar;
import modules.Like;
import modules.ObjektOznameni;
import modules.Obrazek;
import modules.Oznameni;
import modules.OznameniZmena;
import modules.SessionUser;
import modules.Uzivatel;
import sempraceidas2.mainwindow.FXMLDocumentController;
import sempraceidas2.mainwindow.WindowHelper;

/**
 * FXML Controller class
 *
 * @author dzhohar
 */
public class ObrazkyWindowController implements Initializable {

    private DatabaseHelper helper;
    @FXML
    private JFXButton prevBt;

    @FXML
    private JFXButton nextBt;
    @FXML
    private ImageView imageView;
    private int pictureNumber;
    private List<Obrazek> list;
    @FXML
    private JFXTextArea textArea;
    @FXML
    private ImageView lkView;
    @FXML
    private JFXTextField inputText;
    @FXML
    private Label label;
    @FXML
    private JFXButton likeBt;
    @FXML
    private JFXButton deleteImageBtn;
    @FXML
    private JFXButton addImageBtn;
    @FXML
    private JFXButton profileBtn;
    private ArrayList<Komentar> komentare;
    private static int idUzivatele;
    private Uzivatel uzivatel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            helper = DatabaseHelper.getInstance();
            System.out.println(idUzivatele);
            uzivatel = helper.getUserById(idUzivatele);
            System.out.println(uzivatel.getJmeno() + uzivatel.getPrijmeni());
            if (idUzivatele != SessionUser.getUzivatelOnline().getId()) {
                deleteImageBtn.setVisible(false);
                deleteImageBtn.setDisable(true);
                addImageBtn.setVisible(false);
                addImageBtn.setDisable(true);
                profileBtn.setVisible(false);
                profileBtn.setDisable(true);
            }
            textArea.setEditable(false);
            komentare = new ArrayList<>();
            lkView.setImage(new Image("file:src/images/like.png"));

            setImages();
            textArea.setOnContextMenuRequested(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
                    System.out.println("selected text:"
                            + textArea.getSelectedText());
                }
            });
            inputText.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER && !inputText.getText().isEmpty() && list.size() > 0) {

                    System.out.println("#add komentar");
                    int idKom = helper.getID("KOMENTARE_SQ");
                    System.out.println(list.get(pictureNumber).getId());
                    Komentar komentar = new Komentar(idKom, " ", inputText.getText(),
                            new Timestamp(new Date().getTime()), 0, 1, SessionUser.getUzivatelOnline().getId(), list.get(pictureNumber).getId());
                    System.out.println(komentar.getIdObrazku());
                    helper.pridatKomentar(komentar);
                    textArea.appendText(SessionUser.getUzivatelOnline().getPrijmeni() + ":\n" + inputText.getText() + ".\n");
                    if (SessionUser.getUzivatelOnline().getId() != idUzivatele) {
                        int idObjk = helper.getID("OBJEKT_OZNAMENI_SEQ");
                        ObjektOznameni objektOznameni = new ObjektOznameni(idObjk, 2, idKom, komentar.getDate().get(), 0);
                        helper.addOznameniObjekt(objektOznameni);
                        int oznId = helper.getID("oznameni_SEQ");
                        Oznameni oznameni = new Oznameni(oznId, objektOznameni.getId(), list.get(pictureNumber).getIdUzivatele(), 0);
                        helper.addOznameni(oznameni);
                        int oznZmId = helper.getID("oznameni_zmena_SEQ");
                        OznameniZmena oznameniZm = new OznameniZmena(oznZmId, objektOznameni.getId(), komentar.getIdUzivatele(), 0);
                        helper.addOznameniZmena(oznameniZm);
                        inputText.clear();
                    }

                }
            });

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    private void setImages() {

        list = helper.getImages(idUzivatele);
        System.out.println("-----------------");
        System.out.println(list.size());
        System.out.println(pictureNumber);
        pictureNumber = pictureNumber >= list.size() ? --pictureNumber : pictureNumber++;
        if (!list.isEmpty()) {
            System.out.println(pictureNumber);
            imageView.setImage(list.get(pictureNumber).getImage());
            setKomentare();
            setLikes();
        } else {
            imageView.setImage(null);
            likeBt.setText("");
            inputText.clear();
            textArea.clear();
        }
    }

    @FXML
    void handleButtonPridatAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File savedFile = fileChooser.showOpenDialog(null);
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JPG,PNG Files", "*.jpg", "*.png"));
        if (savedFile != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            VBox box = new VBox();
            HBox hbox3 = new HBox();
            HBox hbox4 = new HBox();
            hbox3.setSpacing(15);
            hbox4.setSpacing(15);
            box.setSpacing(10);

            JFXTextField nazevField = new JFXTextField();
            nazevField.setPromptText("Nazev");
            JFXTextField popFieldField = new JFXTextField();
            popFieldField.setPromptText("Popisek");
            alert.setHeaderText("Nastaveni");
            hbox3.getChildren().addAll(nazevField);
            hbox4.getChildren().addAll(popFieldField);
            box.getChildren().addAll(hbox3, hbox4);
            alert.getDialogPane().setContent(box);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK && nazevField.getText().length() > 0) {
                int id = helper.getID("images_sq");
                Image image = new Image(savedFile.toURI().toString());
                Obrazek obrazek = new Obrazek(id, nazevField.getText(), popFieldField.getText(), image, new Timestamp(System.currentTimeMillis()), SessionUser.getUzivatelOnline().getId(), 0);
                helper.addImage(savedFile, obrazek);
                pictureNumber++;
                list.add(obrazek);
                imageView.setImage(image);
                imageView.setImage(list.get(pictureNumber).getImage());
            }

        } else {
            WindowHelper.showAlert("Select image", Alert.AlertType.WARNING);
        }

    }

    @FXML
    public void handleButtonNextAction(ActionEvent event) {
        System.out.println("Next: " + pictureNumber);
        System.out.println(list.size());
        if (!list.isEmpty()) {
            likeControl();
            if (pictureNumber == list.size() - 1) {
                nextBt.setDisable(true);

            } else {
                nextBt.setDisable(false);
                pictureNumber++;
                imageView.setImage(list.get(pictureNumber).getImage());
                setLikes();
                setKomentare();
            }
            prevBt.setDisable(false);
        } else {
            WindowHelper.showAlert("Prazdna galerie", Alert.AlertType.WARNING);
        }

    }

    @FXML
    public void handleButtonPreviousAction(ActionEvent event) {
        System.out.println("Previous: " + pictureNumber);
        if (!list.isEmpty()) {
            likeControl();
            if (pictureNumber > 0) {
                pictureNumber--;
                prevBt.setDisable(false);
                imageView.setImage(list.get(pictureNumber).getImage());
                setKomentare();
                setLikes();
            } else {
                prevBt.setDisable(true);

            }
            nextBt.setDisable(false);
        } else {
            WindowHelper.showAlert("Prazdna galerie", Alert.AlertType.WARNING);

        }

    }

    @FXML
    public void handleButtonOdebratAction(ActionEvent event) {
        System.out.println("Delete");
        System.out.println("Size: " + list.size());
        System.out.println(pictureNumber);
        System.out.println("===========================================================================");
        if (!list.isEmpty()) {
            Obrazek image = list.get(pictureNumber);
            helper.deletePicture(image.getId());
            list.clear();
            setImages();
        } else {
            WindowHelper.showAlert("You have no images to delete", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void handleButtonLikeAction(ActionEvent event) {

        Obrazek image = list.isEmpty() ? null : list.get(pictureNumber);
        if (image != null && !likeControl()) {
            int id = helper.getID("like_sq");
            Like like = new Like(id, new Timestamp(System.currentTimeMillis()), image.getId(), 0, idUzivatele);
            helper.addLike(like);
            int count = helper.getLike(image.getId());
            likeBt.setText(String.valueOf(count));
        } else {
            WindowHelper.showAlert("Forbidden action", Alert.AlertType.WARNING);
        }

    }

    private void setKomentare() {
        textArea.clear();
        inputText.clear();
        komentare = helper.getKomentareObrazku(list.get(pictureNumber).getId());
        if (!komentare.isEmpty()) {

            komentare.forEach(kom -> {
                String tmp = helper.getAuToraKomentare(kom.getIdKom().get());
                String pole[] = tmp.split(";");
                String str = pole[0] + " " + pole[1] + ":\n" + kom.getObsah().get() + ".\n";
                textArea.appendText(str);

            });
        }
    }

    private void setLikes() {
        Obrazek image = list.get(pictureNumber);
        int count = helper.getLike(image.getId());
        likeBt.setText(String.valueOf(count));
    }

    private boolean likeControl() {
        return helper.likeCotnrol(list.get(pictureNumber).getId(), idUzivatele);

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
    void handleButtonProfileImageAction(ActionEvent event) {
        if (!list.isEmpty()) {
            Obrazek image = list.get(pictureNumber);
            helper.setProfileImage(image.getId());
        }

    }

    public static void setIdUzivatele(int idUzivatele) {
        ObrazkyWindowController.idUzivatele = idUzivatele;
    }

}
