/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.mainwindow;

import database.DatabaseHelper;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import modules.ObjektOznameniZmena;

/**
 *
 * @author dzhohar
 */
public class ListCellFactory implements Callback<ListView<ObjektOznameniZmena>, ListCell<ObjektOznameniZmena>> {

    private static DatabaseHelper helper;

    public ListCellFactory() throws SQLException {
        helper = DatabaseHelper.getInstance();

    }

    @Override
    public ListCell<ObjektOznameniZmena> call(ListView<ObjektOznameniZmena> param) {
        return new MyCell();
    }

    private static class MyCell extends ListCell<ObjektOznameniZmena> {

        HBox box = new HBox();
        Label label = new Label();
        Button b = new Button("Delete");
        Button btn = null;

        public MyCell() {
            box.getChildren().addAll(label, b);
            box.setSpacing(20);
            box.setMinWidth(300);
            box.setPadding(new Insets(15));
            System.out.println("Constructor");
        }

        @Override
        public void updateItem(ObjektOznameniZmena item, boolean empty) {
            super.updateItem(item, empty);
            System.out.println(box.getChildren().size());
            btn = new Button("Accept");

            if (empty) {
                setText(null);
                setGraphic(null);
                System.out.println("Empty");
            } else {

                if (item.getNotificationType().trim().equals("FriendRequest") && box.getChildren().size() != 3) {
                    setGraphic(box);

                    box.getChildren().add(btn);

                    btn.setOnAction(e -> {
                        helper.callUpdateFriendshipProcedure(item.getActorId(), item.getNotifierId());
                        helper.odmitnoutZadostOPratelstvi(item.getIdObjectNatification());
                        getListView().getItems().remove(getItem());
                    });
                    label.setText(item.getActorName() + " " + item.getActorSurname() + " chce s vami kamaradit");
                    setGraphic(box);
                } else if (item.getNotificationType().trim().equals("Zprava")) {
                    setGraphic(box);
                    label.setText("Mate novou zpravu od " + item.getActorName() + " " + item.getActorSurname());
                    setGraphic(box);
                } else {
                    String nazev = helper.getNazevSkupiny(item.getObjectNatifEntityId());
                    String tmp = "Mate novy komentar  od " + item.getActorName() + " " + item.getActorSurname();
                    if(!nazev.equals("null")){
                       tmp += " v skupine "+ nazev;
                    }
                    label.setText(tmp);
                    setGraphic(box);
                }
                b.setOnAction(e -> {
                    helper.odmitnoutZadostOPratelstvi(item.getIdObjectNatification());
                    getListView().getItems().remove(getItem());
                });
            }

        }

    }

}
