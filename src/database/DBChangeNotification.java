/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.jfoenix.controls.JFXButton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import modules.ObjektOznameniZmena;
import modules.SessionUser;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import sempraceidas2.mainwindow.FXMLDocumentController;

/**
 *
 * @author dzhohar
 */
public class DBChangeNotification {

    private static DBChangeNotification dBChangeNotification;
    private static ScheduledService<Integer> service;
    private static ArrayList<ObjektOznameniZmena> list;
    private static int count;
    private DatabaseHelper databaseHelper;
    private JFXButton button;

    private DBChangeNotification() {
        try {
            databaseHelper = DatabaseHelper.getInstance();
            service = new ScheduledService<Integer>() {
                @Override
                protected Task<Integer> createTask() {
                    return new Task<Integer>() {
                        @Override
                        protected Integer call() throws Exception {
                            list = databaseHelper.getOznameniById(SessionUser.getUzivatelOnline().getId());
                            count = databaseHelper.getCountsOfNotificaion(SessionUser.getUzivatelOnline().getId());

                            Platform.runLater(() -> {
                                if (button != null) {
                                    setText();

                                }

                            });
                            return 1;
                        }
                    };
                }
            };
            service.setDelay(Duration.seconds(2));
            service.setPeriod(Duration.seconds(5));
            service.start();
        } catch (SQLException ex) {
            Logger.getLogger(DBChangeNotification.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setText() {

        button.setText(count == 0 ? "" : String.valueOf(count));
    }

    public static DBChangeNotification getInstance() {
        return dBChangeNotification;
    }

    public void setButton(JFXButton button) {
        this.button = button;
    }

    public static void startListener() {
        if (dBChangeNotification == null) {
            dBChangeNotification = new DBChangeNotification();
        }
    }

    public static ArrayList<ObjektOznameniZmena> getNotifications() {
        return list;
    }

    public static int getCount() {
        return count;
    }

}
