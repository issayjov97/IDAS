/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dzhohar
 */
public class SessionUser {

    private static Uzivatel uzivatelOnlne;
    private static Uzivatel uzivatelReal;

    public static Uzivatel getUzivatelOnline() {
        return uzivatelOnlne;
    }

    public static Uzivatel getUzivatelReal() {
        return uzivatelReal;
    }

    public static void setUzivatel(Uzivatel uz) {

        if (uzivatelReal == null) {
            uzivatelReal = uz;
        }
        SessionUser.uzivatelOnlne = uz;

    }

    public static void unsetUzivatel() {
        uzivatelOnlne = null;
        uzivatelReal = null;
    }

}
