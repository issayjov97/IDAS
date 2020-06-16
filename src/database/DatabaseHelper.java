package database;

import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.sql.Types;
import java.util.Date;
import java.util.concurrent.Callable;
import modules.Komentar;
import modules.StudijniPlan;
import modules.Kontakt;
import modules.Like;
import modules.ObjektOznameni;
import modules.ObjektOznameniZmena;
import modules.Obrazek;
import modules.Oznameni;
import modules.OznameniZmena;
import modules.Podminka;
import modules.Predmet;
import modules.Role;
import modules.Skupina;
import modules.StudijniObor;
import modules.Udalost;
import modules.Uzivatel;
import modules.Zapocet;
import modules.Zprava;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import sempraceidas2.mainwindow.WindowHelper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dzhohar
 */
public class DatabaseHelper extends Configs implements DatabaseChangeListener {

    private static Connection connection;
    private static DatabaseHelper databaseHelper = null;
    private ResultSet result = null;
    private PreparedStatement ps = null;

    private String qu;

    private DatabaseHelper() throws SQLException {

        connection = DriverManager.getConnection(DATABASE_URL + ":" + HOSTNAME + ":" + PORT + ":" + SID, USERNAME, PASSWORD);
    }

    public static DatabaseHelper getInstance() throws SQLException {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;

    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {

            connection = DriverManager.getConnection(DATABASE_URL + ":" + HOSTNAME + ":" + PORT + ":" + SID, USERNAME, PASSWORD);

        }
        return connection;
    }

    public static void closeConnection() throws SQLException {

        connection.close();

    }

    public ArrayList<Uzivatel> findUser(String jmeno, String prijmeni, Integer id) {
        try {
            ArrayList<Uzivatel> uzivatele = new ArrayList<>();
            qu = "SELECT id_uzivatele, login, heslo, jmeno, prijmeni, poznamka, email, blokace FROM UZIVATELE WHERE "
                    + "id_uzivatele = ? OR jmeno = ? OR prijmeni =? ";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setString(2, jmeno);
            ps.setString(3, prijmeni);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                uzivatele.add(uzivatel);
            }
            result.close();

            connection.commit();

            return uzivatele;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public Uzivatel getUser(String login, String heslo) throws SQLException {
        qu = "SELECT * FROM UZIVATELE WHERE "
                + "login = ?  and heslo = ?";
        ps = connection.prepareStatement(qu);
        ps.setString(1, login);
        ps.setString(2, heslo);
        result = ps.executeQuery();
        while (result.next()) {
            int id = result.getInt("id_uzivatele");
            String loginUz = result.getString("login");
            String hesloUz = result.getString("heslo");
            String jm = result.getString("jmeno");
            String fm = result.getString("prijmeni");
            String poznamka = result.getString("poznamka");
            String email = result.getString("email");
            Integer blokace = result.getInt("blokace");
            Uzivatel uzivatel = new Uzivatel(id, loginUz, hesloUz, jm, fm, poznamka, email, blokace);
            result.close();
            connection.commit();
            System.out.println(uzivatel);
            return uzivatel;

        }

        return null;
    }

    public Uzivatel getUserById(int id) throws SQLException {
        qu = "SELECT id_uzivatele, login, heslo, jmeno, prijmeni, poznamka, email, blokace FROM UZIVATELE WHERE "
                + "id_uzivatele=?";
        ps = connection.prepareStatement(qu);
        ps.setInt(1, id);
        result = ps.executeQuery();
        while (result.next()) {
            int idUz = result.getInt("id_uzivatele");
            String loginUz = result.getString("login");
            String heslo = result.getString("heslo");
            String jm = result.getString("jmeno");
            String fm = result.getString("prijmeni");
            String poznamka = result.getString("poznamka");
            String email = result.getString("email");
            Integer blokace = result.getInt("blokace");
            Uzivatel uzivatel = new Uzivatel(idUz, loginUz, heslo, jm, fm, poznamka, email, blokace);
            result.close();
            connection.commit();

            return uzivatel;
        }

        return null;
    }

    public Uzivatel getAutoraZpravy(int id) {
        try {
            qu = "select uz.* from uzivatele uz \n"
                    + " inner join KORESPONDENCE kor on kor.ID_ODESILATEL = uz.ID_UZIVATELE\n"
                    + " inner join zpravy zp on zp.id_zpravy = kor.id_zpravy\n"
                    + " where zp.id_zpravy =?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idUz = result.getInt(1);
                String loginUz = result.getString(2);
                String heslo = result.getString(3);
                String jm = result.getString(4);
                String fm = result.getString(5);
                String poznamka = result.getString(6);
                String email = result.getString(7);
                Integer blokace = result.getInt(8);
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, heslo, jm, fm, poznamka, email, blokace);
                connection.commit();
                result.close();
                return uzivatel;
            }

            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Uzivatel getAdresatZpravy(int id) {
        try {
            qu = "select uz.* from uzivatele uz \n"
                    + "inner join KORESPONDENCE kor on kor.ID_Adresat = uz.ID_UZIVATELE\n"
                    + "inner join zpravy zp on zp.id_zpravy = kor.id_zpravy\n"
                    + "where zp.id_zpravy = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idUz = result.getInt(1);
                String loginUz = result.getString(2);
                String heslo = result.getString(3);
                String jm = result.getString(4);
                String fm = result.getString(5);
                String poznamka = result.getString(6);
                String email = result.getString(7);
                Integer blokace = result.getInt(8);
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, heslo, jm, fm, poznamka, email, blokace);
                return uzivatel;
            }
            result.close();
            connection.commit();

            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Udalost> getUdalost(int idUziv) throws SQLException {
        ArrayList<Udalost> listUdalosti = new ArrayList<>();
        String qu = "SELECT id_udalosti, datum, obsah, nazev,id_uzivatele, misto_konani FROM UDALOSTI WHERE id_uzivatele=?";
        ps = connection.prepareStatement(qu);
        ps.setInt(1, idUziv);
        result = ps.executeQuery();
        while (result.next()) {
            int idUdal = result.getInt("id_udalosti");
            Timestamp date = result.getTimestamp("datum");
            String obsah = result.getString("obsah");
            String nazev = result.getString("nazev");
            int idUzivatele = result.getInt("id_uzivatele");
            String mistoKonani = result.getString("misto_konani");
            Udalost udalost = new Udalost(idUdal, date, obsah, nazev, idUzivatele, mistoKonani);
            listUdalosti.add(udalost);
        }
        result.close();
        connection.commit();

        return listUdalosti;
    }

    public boolean insertMessage(int zpravaID, String obsah, String poznamka, Timestamp time, String predmet) {
        try {
            qu = "insert into zpravy values(?,?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, zpravaID);
            ps.setTimestamp(2, time);
            ps.setString(3, obsah);
            ps.setString(4, poznamka);
            ps.setString(5, predmet);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    public ArrayList<StudijniPlan> getPlan(int idUzivatele) {
        ArrayList<StudijniPlan> plany = new ArrayList<>();
        try {
            StudijniPlan plan = null;
            qu = "Select id_planu, nazev, id_oboru, popis  from studijni_plany sp"
                    + " inner join uzivatele_sp uzsp on sp.id_planu = uzsp.id_st_planu "
                    + " where uzsp.id_uzivatele = ? ";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idUzivatele);
            result = ps.executeQuery();
            while (result.next()) {

                int id = result.getInt(1);
                String nazev = result.getString(2);
                int idOboru = result.getInt(3);
                String popis = result.getString(4);
                plan = new StudijniPlan(id, nazev, idOboru, popis);
                plany.add(plan);
            }
            connection.commit();

            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public StudijniObor getOborByID(int id) {

        try {
            StudijniObor obor = null;
            qu = "Select * "
                    + " from studijni_obory"
                    + " where id_oboru = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idOboru = result.getInt(1);
                String nazev = result.getString(2);
                String popis = result.getString(3);
                obor = new StudijniObor(idOboru, nazev, popis);
            }
            connection.commit();

            return obor;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public boolean insertZpravyKontakty(int odesilatelID, int zpravaID, int adresatID) {
        try {

            int id = getID("korespondence_id");
            qu = "INSERT INTO KORESPONDENCE VALUES(?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, odesilatelID);
            ps.setInt(3, zpravaID);
            ps.setInt(4, adresatID);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public int getID(String sequence) {
        try {

            qu = "Select " + sequence + ".nextval from dual";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();

            result.next();
            int id = result.getInt(1);
            return id;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return -1;
        }

    }

    public boolean changeJmeno(int id, String jmeno) {
        try {
            qu = "UPDATE UZIVATELE SET jmeno = ?  where id_uzivatele=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, jmeno);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ResultSet execQuery(String query) {

        try {
            if (query == null || query.equals(Const.emptyRegex)) {
                WindowHelper.showAlert("Chyba", Alert.AlertType.ERROR);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            result.close();
            connection.commit();

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString() + "executeQuery", Alert.AlertType.ERROR);
            return null;
        }
        return result;
    }

    public boolean changePrijmeni(int id, String prijmeni) {
        try {
            qu = "UPDATE UZIVATELE SET prijmeni=?  where id_uzivatele=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, prijmeni);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeEmail(int id, String email) {
        try {
            qu = "UPDATE UZIVATELE SET email=?  where id_uzivatele=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, email);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeHeslo(int id, String heslo) {
        try {
            qu = "UPDATE UZIVATELE SET heslo=?  where id_uzivatele=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, heslo);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public void callUpdateFriendshipProcedure(int id1, int id2) {

        try {
            ps = connection.prepareCall("{call update_firendship(?,?)}");
            ps.setInt(1, id1);
            ps.setInt(2, id2);

            ps.execute();
            System.out.println("Stored Procedure executed successfully");
            connection.commit();

        } catch (SQLException err) {
            WindowHelper.showAlert(err.toString(), Alert.AlertType.ERROR);

        }

    }

    public int callUserLoginProcedure(String login, String password) {

        try {
            CallableStatement cstmt = connection.prepareCall("{call USER_LOGIN(?,?,?)}");
            cstmt.setString(1, login);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.execute();
            int res = cstmt.getInt(3);
            connection.commit();

            return res;
        } catch (SQLException err) {
            err.getCause().getMessage();

        }
        return -1;
    }

    public String callAddUpdateGroupProcedure(Skupina skupina, int idPredmetu) {

        try {
            CallableStatement cstmt = connection.prepareCall("{call add_update_group(?,?,?,?,?)}");
            cstmt.setInt(1, skupina.getId());
            cstmt.setString(2, skupina.getNazev());
            cstmt.setInt(3, skupina.getKomentar());
            cstmt.setInt(4, idPredmetu);
            cstmt.registerOutParameter(5, Types.CLOB);
            cstmt.execute();
            String message = cstmt.getString(5);
            cstmt.close();
            return message;
        } catch (SQLException err) {
            WindowHelper.showAlert(err.toString(), Alert.AlertType.ERROR);

        }
        return null;

    }

    public int callFunctionAreFriends(int id1, int id2) {

        try {
            int resultInt;
            try (CallableStatement cstmt = connection.prepareCall("{? = call AREFRIENDS(?,?)}")) {
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.setInt(2, id1);
                cstmt.setInt(3, id2);
                cstmt.execute();
                resultInt = cstmt.getInt(1);
                cstmt.close();
            }

            connection.commit();

            return resultInt;

        } catch (SQLException err) {
            WindowHelper.showAlert(err.toString(), Alert.AlertType.ERROR);

        }
        return 0;
    }

    public int callFunctionAddUpdateUser(String login, String heslo, String email, String jmeno, String prijmeni) {

        try {
            int resultInt;
            try (CallableStatement cstmt = connection.prepareCall("{? = call ADD_UPDATE_USER(?,?,?,?,?,?)}")) {
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.setNull(2, Types.INTEGER);
                cstmt.setString(3, jmeno);
                cstmt.setString(4, login);
                cstmt.setString(5, prijmeni);
                cstmt.setString(6, heslo);
                cstmt.setString(7, email);
                cstmt.execute();
                resultInt = cstmt.getInt(1);
            }
            connection.commit();

            return resultInt;

        } catch (SQLException err) {
            WindowHelper.showAlert(err.toString(), Alert.AlertType.ERROR);

        }
        return 0;
    }

    public int callFunctionIsUserBlocked(int idUz) {

        try {
            int resultInt;
            try (CallableStatement cstmt = connection.prepareCall("{? = call ISUSERBLOCKED(?)}")) {
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.setInt(2, idUz);
                cstmt.execute();
                resultInt = cstmt.getInt(1);
                cstmt.close();
            }

            return resultInt;

        } catch (SQLException err) {
            WindowHelper.showAlert(err.toString(), Alert.AlertType.ERROR);

        }
        return 0;
    }

    public ResultSet executeUpdate(String query) {
        ResultSet resultSet = null;
        try {
            if (query == null || query.equals(Const.emptyRegex)) {
                WindowHelper.showAlert("Chyba", Alert.AlertType.ERROR);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            connection.commit();

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
        return resultSet;
    }

    public ArrayList<Uzivatel> getFriends(int id) {
        try {
            ArrayList<Uzivatel> friends = new ArrayList<>();
            qu = "SELECT uz.* from uzivatele uz"
                    + " inner join friends on uz.id_uzivatele = friends.id_uzivatele2"
                    + " where friends.id_uzivatele1 = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                friends.add(uzivatel);
            }
            connection.commit();

            return friends;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);

        }
        return null;
    }

    public boolean pridatUzivatele(Uzivatel uzivatel) {
        try {
            qu = "INSERT INTO UZIVATELE VALUES(?,?,?,?,?,?,?,?)";
            ps = connection.prepareCall(qu);
            ps.setInt(1, uzivatel.getId());
            ps.setString(2, uzivatel.getLogin());
            ps.setString(3, uzivatel.getHeslo());
            ps.setString(4, uzivatel.getJmeno());
            ps.setString(5, uzivatel.getPrijmeni());
            ps.setString(6, uzivatel.getPoznamka());
            ps.setString(7, uzivatel.getEmail());
            ps.setInt(8, 1);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Uzivatel> getUsers(int id) throws SQLException {
        ArrayList<Uzivatel> list = new ArrayList<>();

        qu = "SELECT id_uzivatele, login, heslo, jmeno, prijmeni, poznamka, email, blokace FROM UZIVATELE WHERE "
                + "id_uzivatele = ?";
        ps = connection.prepareStatement(qu);
        ps.setInt(1, id);
        result = ps.executeQuery();
        while (result.next()) {
            int idUz = result.getInt("id_uzivatele");
            String loginUz = result.getString("login");
            String heslo = result.getString("heslo");
            String jm = result.getString("jmeno");
            String fm = result.getString("prijmeni");
            String poznamka = result.getString("poznamka");
            String email = result.getString("email");
            Integer blokace = result.getInt("blokace");
            Uzivatel uzivatel = new Uzivatel(id, loginUz, heslo, jm, fm, poznamka, email, blokace);
            list.add(uzivatel);
        }
        connection.commit();

        return list;

    }

    public boolean odebratUdalost(int idUz, int idUdal) {
        try {
            qu = "delete from udalosti "
                    + " where id_uzivatele = ? and id_udalosti = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idUz);
            ps.setInt(2, idUdal);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean deleteKontact(int idKont, int idUZ) {
        try {
            qu = "delete from friends where id_uzivatele2 = ? AND id_uzivatele1 = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idKont);
            ps.setInt(2, idUZ);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Zprava> getZpravy(int id) {
        try {
            ArrayList<Zprava> list = new ArrayList<>();
            qu = "select z.* from zpravy z"
                    + " inner join korespondence kor ON z.id_zpravy = kor.id_zpravy"
                    + " inner join uzivatele uz ON uz.id_uzivatele = kor.id_adresat"
                    + " where kor.id_adresat = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idZp = result.getInt("id_zpravy");
                Timestamp datum = result.getTimestamp("casove_razitko");
                String obsah = result.getString("obsah");
                String poznamka = result.getString("poznamka");
                String predmet = result.getString("predmet");
                Zprava zprava = new Zprava(idZp, datum, obsah, poznamka, predmet);
                list.add(zprava);

            }
            connection.commit();

            return list;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;

        }
    }

    public ArrayList<Zprava> getPrijateZpravy(int id) {
        try {
            ArrayList<Zprava> list = new ArrayList<>();
            qu = "select z.* from zpravy z\n"
                    + " inner join korespondence kor ON z.id_zpravy = kor.id_zpravy \n"
                    + " inner join uzivatele uz ON uz.id_uzivatele = kor.id_adresat\n"
                    + " where kor.ID_ODESILATEL = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idZp = result.getInt("id_zpravy");
                Timestamp datum = result.getTimestamp("casove_razitko");
                String obsah = result.getString("obsah");
                String poznamka = result.getString("poznamka");
                String predmet = result.getString("predmet");
                Zprava zprava = new Zprava(idZp, datum, obsah, poznamka, predmet);
                list.add(zprava);

            }
            return list;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;

        }
    }

    public boolean addPlan(ArrayList<StudijniPlan> plany, int id) {
        try {
            for (StudijniPlan value : plany) {
                qu = "insert into uzivatele_sp VALUES(UZIVATELE_SP_ID.nextval,?,?)";
                ps = connection.prepareStatement(qu);
                ps.setInt(1, id);
                ps.setInt(2, value.getId());
                ps.executeUpdate();
            }
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Skupina> getSkupiny(int id) {
        ArrayList<Skupina> skupiny = new ArrayList<>();
        try {
            qu = "select sk.* from skupiny sk\n"
                    + " inner join skupina_cleny sk_cleny on sk_cleny.id_skupiny = sk.id_skupiny\n"
                    + " where sk_cleny.id_uzivatele =?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idSk = result.getInt(1);
                String nazevSk = result.getString(2);
                int idKomen = result.getInt(3);
                Skupina skupina = new Skupina(idSk, nazevSk, idKomen);
                skupiny.add(skupina);
            }
            connection.commit();

            return skupiny;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Skupina> getSkupinyVyucujiciho(int id) {
        ArrayList<Skupina> skupiny = new ArrayList<>();
        try {
            qu = "select DISTINCT sk.* from skupiny sk\n"
                    + " inner join skupiny_predmety psp on psp.id_skupiny = sk.ID_SKUPINY\n"
                    + " inner join predmety on predmety.id_predmetu = psp.id_predmetu\n"
                    + " inner join predmety_vyucujici pred_vyuc on pred_vyuc.id_predmetu = predmety.ID_PREDMETU \n"
                    + " where pred_vyuc.id_vyucujiciho = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idSk = result.getInt(1);
                String nazevSk = result.getString(2);
                int idKomen = result.getInt(3);
                Skupina skupina = new Skupina(idSk, nazevSk, idKomen);
                skupiny.add(skupina);
            }
            result.close();

            connection.commit();

            return skupiny;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Skupina> getSkupinyPredmetu(int id) {
        ArrayList<Skupina> skupiny = new ArrayList<>();
        try {
            qu = "select sk.* from skupiny sk\n"
                    + " inner join skupiny_predmety psp on psp.id_skupiny = sk.ID_SKUPINY\n"
                    + " where psp.id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idSk = result.getInt(1);
                String nazevSk = result.getString(2);
                int idKomen = result.getInt(3);
                Skupina skupina = new Skupina(idSk, nazevSk, idKomen);
                skupiny.add(skupina);
            }
            result.close();

            connection.commit();

            return skupiny;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public Komentar getKomentar(int id) {
        try {
            Komentar komentar = null;
            qu = "Select * from komentare"
                    + " where id_komentare=? ";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idKom = result.getInt(1);
                Timestamp date = result.getTimestamp(2);
                String obsah = result.getString(3);
                int idPodKomen = result.getInt(4);
                int blokace = result.getInt(5);
                String nazev = result.getString(6);
                int idUziv = result.getInt(7);
                int idObrazku = result.getInt(8);
                komentar = new Komentar(idKom, nazev, obsah, date, idPodKomen, blokace, idUziv, idObrazku);
            }
            result.close();

            connection.commit();

            return komentar;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public String getAuToraKomentare(int id) {
        try {
            String tmp = null;
            qu = "select uz.jmeno,uz.prijmeni from komentare kom\n"
                    + " inner join uzivatele uz on kom.id_uzivatele = uz.id_uzivatele\n"
                    + " where kom.ID_KOMENTARE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                String jmeno = result.getString(1);
                String prijmeni = result.getString(2);
                tmp = jmeno + ";" + prijmeni;
            }
            connection.commit();

            return tmp;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Komentar> getKomentareObrazku(int idObrazu) {
        try {
            ArrayList<Komentar> list = new ArrayList<>();
            Komentar komentar = null;
            qu = "Select * from komentare"
                    + " where id_obrazku=? ";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idObrazu);
            result = ps.executeQuery();
            while (result.next()) {
                int idKom = result.getInt(1);
                Timestamp date = result.getTimestamp(2);
                String obsah = result.getString(3);
                int idPodKomen = result.getInt(4);
                int blokace = result.getInt(5);
                String nazev = result.getString(6);
                int idUziv = result.getInt(7);
                int idObrazku = result.getInt(8);
                komentar = new Komentar(idKom, nazev, obsah, date, idPodKomen, blokace, idUziv, idObrazku);
                list.add(komentar);
            }
            result.close();

            connection.commit();

            return list;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Komentar> getPodKomentars(int id) {
        try {
            ArrayList<Komentar> list = new ArrayList<>();
            qu = "Select *"
                    + " from komentare kom"
                    + " start with kom.id_komentare = ?"
                    + " connect by prior kom.id_komentare=kom.id_podkomentare";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();

            while (result.next()) {
                int idKom = result.getInt(1);
                Timestamp date = result.getTimestamp(2);
                String obsah = result.getString(3);
                int idPodKomen = result.getInt(4);
                int blokace = result.getInt(5);
                String nazev = result.getString(6);
                int idUziv = result.getInt(7);
                int idObr = result.getInt(8);
                Komentar komentar = new Komentar(idKom, nazev, obsah, date, idPodKomen, blokace, idUziv, idObr);
                list.add(komentar);
            }
            connection.commit();

            return list;
        } catch (SQLException ex) {
            return null;
        }
    }

    public boolean deleteZpravu1(int id) {
        try {
            qu = "delete from korespondence where id_zpravy = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            int res = ps.executeUpdate();
            qu = "delete from zpravy where id_zpravy = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            res = ps.executeUpdate();
            deleteZpravu2(id);
            connection.commit();
            result.close();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean deleteZpravu2(int id) {
        try {

            qu = "delete from zpravy where id_zpravy = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean pridatClenaSkupiny(int idSk, int idUz) {
        try {
            qu = "insert into skupina_cleny values(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idSk);
            ps.setInt(2, idUz);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatUdalost(Udalost udalost) {
        try {
            qu = "insert into udalosti values(udalosti_sq.nextval,?,?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setTimestamp(1, udalost.getDatum());
            ps.setString(2, udalost.getObsah());
            ps.setString(3, udalost.getNazev());
            ps.setInt(4, udalost.getIdUziv());
            ps.setString(5, udalost.getMistoKonani());
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Role> getRole(int idUz) {
        ArrayList<Role> roles = new ArrayList<>();
        try {
            qu = "select * from roles "
                    + "where roles.id_uzivatele = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idUz);
            result = ps.executeQuery();
            while (result.next()) {
                int idRole = result.getInt(1);
                String nazev = result.getString(2);
                String opravneni = result.getString(3);
                String poznamka = result.getString(4);
                Role role = new Role(idRole, nazev, poznamka, opravneni);
                roles.add(role);
            }
            result.close();

            connection.commit();

            return roles;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Role> getAllRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        try {
            qu = "select * from role";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int idRole = result.getInt(1);
                String nazev = result.getString(2);
                String opravneni = result.getString(3);
                String poznamka = result.getString(4);
                Role role = new Role(idRole, nazev, poznamka, opravneni);
                roles.add(role);
            }
            result.close();

            connection.commit();

            return roles;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Uzivatel> getNoveClenySkupiny(int id) {
        ArrayList<Uzivatel> users = new ArrayList<>();
        try {
            qu = "select distinct uz.*\n"
                    + " from skupina_cleny sk_cl\n"
                    + " right join uzivatele uz on  sk_cl.id_uzivatele = uz.id_uzivatele\n"
                    + " inner join role_uzivatele rol_uz on uz.id_uzivatele =rol_uz.ID_UZIVATELE\n"
                    + " where rol_uz.id_role =? and (sk_cl.ID_SKUPINY is null or sk_cl.ID_SKUPINY<> ? )";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, 88);
            ps.setInt(2, id);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                users.add(uzivatel);
            }
            result.close();

            connection.commit();

            return users;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPredmety() {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            qu = "select * from predmety";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int idPr = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                Predmet predmet = new Predmet(idPr, zkratka, nazev);
                predmety.add(predmet);
            }
            return predmety;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPredmetySkupinyById(int id) {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            qu = "select predmety.* from predmety\n"
                    + " where id_predmetu not IN(Select id_predmetu from \n"
                    + " skupiny_predmety where id_skupiny = ?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPr = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                Predmet predmet = new Predmet(idPr, zkratka, nazev);
                predmety.add(predmet);
            }
            result.close();

            connection.commit();

            return predmety;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPredmetyByID(ArrayList<Integer> id) {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            qu = "select * from predmety where id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            for (Integer idPred : id) {
                ps.setInt(1, idPred);
                result = ps.executeQuery();
                while (result.next()) {
                    int idPr = result.getInt(1);
                    String zkratka = result.getString(2);
                    String nazev = result.getString(3);
                    Predmet predmet = new Predmet(idPr, zkratka, nazev);
                    predmety.add(predmet);
                }
            }
            result.close();

            connection.commit();

            return predmety;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPredmetySkupiny(int id) {
        try {
            ArrayList<Predmet> predmety = new ArrayList<>();

            qu = "select predmety.* from skupiny_predmety prd\n"
                    + " inner join predmety on prd.id_predmetu = predmety.id_predmetu\n"
                    + "where prd.id_skupiny = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPr = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                Predmet predmet = new Predmet(idPr, zkratka, nazev);
                predmety.add(predmet);
            }
            return predmety;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Uzivatel> getVycujiciPredme(int id) {
        try {
            ArrayList<Uzivatel> vuycujici = new ArrayList<>();

            qu = "SELECT UZ.* FROM UZIVATELE UZ "
                    + " INNER JOIN predmety_vyucujici pv on pv.id_vyucujiciho = uz.id_uzivatele  "
                    + " where  pv.id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String heslo = result.getString("heslo");
                String jm = result.getString("jmeno");
                String fm = result.getString("prijmeni");
                String poznamka = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokace = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, heslo, jm, fm, poznamka, email, blokace);
                vuycujici.add(uzivatel);
            }
            connection.commit();

            return vuycujici;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<StudijniPlan> getAllPlans() {
        ArrayList<StudijniPlan> plany = new ArrayList<>();
        try {
            StudijniPlan plan = null;
            qu = "Select *  from studijni_plany";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt(1);
                String nazev = result.getString(2);
                int idOboru = result.getInt(3);
                String popis = result.getString(4);
                plan = new StudijniPlan(id, nazev, idOboru, popis);
                plany.add(plan);
            }
            connection.commit();

            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public StudijniPlan getPlanByID(int idPl) {

        try {
            StudijniPlan plan = null;
            qu = "Select *  from studijni_plany where id_planu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPl);
            result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt(1);
                String nazev = result.getString(2);
                int idOboru = result.getInt(3);
                String popis = result.getString(4);
                plan = new StudijniPlan(id, nazev, idOboru, popis);
            }
            connection.commit();

            return plan;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<StudijniPlan> getPlanyOboru(int idOboru) {

        try {
            ArrayList<StudijniPlan> plany = new ArrayList<>();
            qu = "Select *  from studijni_plany where id_oboru = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idOboru);
            result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt(1);
                String nazev = result.getString(2);
                int idObr = result.getInt(3);
                String popis = result.getString(4);
                StudijniPlan plan = new StudijniPlan(id, nazev, idObr, popis);
                plany.add(plan);
            }
            connection.commit();
            System.out.println("Plan");
            System.out.println(plany);
            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<StudijniPlan> getNovePlanyOboru(int idOboru) {

        try {
            ArrayList<StudijniPlan> plany = new ArrayList<>();
            qu = "Select *  from studijni_plany where id_oboru <> ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idOboru);
            result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt(1);
                String nazev = result.getString(2);
                int idObr = result.getInt(3);
                String popis = result.getString(4);
                StudijniPlan plan = new StudijniPlan(id, nazev, idObr, popis);
                plany.add(plan);
            }
            connection.commit();
            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<StudijniObor> getObory() {
        ArrayList<StudijniObor> obory = new ArrayList<>();
        try {
            qu = "Select * from studijni_obory ";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int idOboru = result.getInt(1);
                String nazev = result.getString(2);
                String popis = result.getString(3);
                StudijniObor obor = new StudijniObor(idOboru, nazev, popis);
                obory.add(obor);
            }
            result.close();

            connection.commit();

            return obory;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public boolean changeNazevPlanu(int id, String nazev) {
        try {
            qu = "UPDATE STUDIJNI_PLANY SET nazev=?  where id_planu=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changePopisPlanu(int id, String popis) {
        try {
            qu = "UPDATE STUDIJNI_PLANY SET popis=?  where id_planu=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, popis);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeIdOboruPlan(int id, int cislo) {
        try {
            qu = "UPDATE STUDIJNI_PLANY SET id_oboru=?  where id_planu=?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, cislo);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeNazevOboru(int id, String nazev) {
        try {
            qu = "UPDATE STUDIJNI_OBORY SET nazev=?  where id_oboru=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changePopisOboru(int id, String nazev) {
        try {
            qu = "UPDATE STUDIJNI_OBORY SET popis=?  where id_oboru=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeNazevPredmetu(int id, String nazev) {
        try {
            qu = "UPDATE PREDMETY SET nazev=?  where id_predmetu=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeZkratkaPredmetu(int id, String nazev) {
        try {
            qu = "UPDATE PREDMETY SET zkratka=?  where id_predmetu=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changePlanPredmetu(int id, String nazev) {
        try {
            qu = "UPDATE PREDMETY_SP SET id_planu=?  where id_predmetu=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            result.close();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeNazevRole(int id, String nazev) {
        try {
            qu = "UPDATE ROLE SET nazev=?  where id_role=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changePoznamkaRole(int id, String nazev) {
        try {
            qu = "UPDATE ROLE SET poznamka=?  where id_role=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeOpravneniRole(int id, String nazev) {
        try {
            qu = "UPDATE ROLE SET opravneni=?  where id_role=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatRole(Role role) {
        try {
            qu = "INSERT INTO ROLE   VALUES(?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, role.getIdRole());
            ps.setString(2, role.getNazev());
            ps.setString(3, role.getPoznamka());
            ps.setString(4, role.getOpravneni());
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitRole(int id) {
        try {
            qu = "DELETE ROLE WHERE ID_ROLE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserEmail(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET EMAIL = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserHeslo(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET HESLO = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserPoznamka(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET POZNAMKA = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserLogin(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET LOGIN = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserBlokace(int id, int blokace) {
        try {
            qu = "UPDATE UZIVATELE SET BLOKACE = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, blokace);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserJmeno(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET JMENO = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeUserPrijmeni(int id, String newValue) {
        try {
            qu = "UPDATE UZIVATELE SET PRIJMENI = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Uzivatel> getAllUsers() {
        ArrayList<Uzivatel> users = new ArrayList<>();
        try {
            qu = "Select * from uzivatele ";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                users.add(uzivatel);
            }
            result.close();

            connection.commit();

            return users;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public boolean pridatSkupiny(Skupina skupina) {
        try {
            qu = "INSERT INTO SKUPINY VALUES(?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, skupina.getId());
            ps.setString(2, skupina.getNazev());
            ps.setString(3, "");
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitSkupinu(int id) {
        try {
            qu = "DELETE SKUPINY WHERE ID_SKUPINY = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatPredmet(Predmet predmet) {
        try {
            qu = "INSERT INTO PREDMETY VALUES(?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, predmet.getIdPredmetu());
            ps.setString(2, predmet.getZkratka());
            ps.setString(3, predmet.getNazev());
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitPredmet(int id) {
        try {
            qu = "DELETE PREDMETY WHERE ID_PREDMETU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odebratUzivatele(int id) {
        try {
            qu = "DELETE UZIVATELE WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatPlan(StudijniPlan stPaln) {
        try {

            qu = "INSERT INTO STUDIJNI_PLANY VALUES(?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, stPaln.getId());
            ps.setString(2, stPaln.getNazev());
            ps.setInt(3, stPaln.getIdOboru());
            ps.setString(4, stPaln.getPopis());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitPlan(int id) {
        try {
            qu = "DELETE STUDIJNI_PLANY WHERE ID_PLANU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitPlanOboru(int id) {
        try {
            qu = "DELETE STUDIJNI_PLANY WHERE ID_PLANU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatObor(StudijniObor stObor) {
        try {

            qu = "INSERT INTO STUDIJNI_OBORY VALUES(?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, stObor.getId());
            ps.setString(2, stObor.getNazev());
            ps.setString(3, stObor.getPopis());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean odstranitObor(int id) {
        try {
            qu = "DELETE STUDIJNI_OBORY WHERE ID_OBORU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<String> nazvyPlanu(int idOboru) {
        try {
            ArrayList<String> nazvy = new ArrayList<>();
            qu = "Select nazev from studijni_plany"
                    + " where id_oboru = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idOboru);
            result = ps.executeQuery();

            while (result.next()) {
                nazvy.add(result.getString("nazev"));
            }
            result.close();

            return nazvy;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<String> nazvyOboru() {
        try {
            ArrayList<String> nazvy = new ArrayList<>();
            qu = "Select nazev from studijni_obory";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();

            while (result.next()) {
                nazvy.add(result.getString("nazev"));
            }
            connection.commit();

            return nazvy;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public int getIdOboru(String nazev) {
        try {
            qu = "Select id_oboru from Studijni_obory"
                    + " where nazev = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            result = ps.executeQuery();
            result.next();
            int id = result.getInt(1);
            connection.commit();
            result.close();
            return id;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return -1;
        }
    }

    public boolean pridatRole(int idUz, int role) {
        try {

            qu = "INSERT INTO ROLE_UZIVATELE VALUES(UZIV_ROLE.NEXTVAL,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, role);
            ps.setInt(2, idUz);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public int getIdRole(String nazev) {
        try {
            qu = "Select id_role from Role"
                    + " where nazev = ?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            result = ps.executeQuery();
            result.next();
            connection.commit();
            int id = result.getInt(1);

            return id;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return -1;
        }
    }

    public boolean pridatPredmetySp(int idPlanu, int idPredmetu) {
        try {
            qu = "insert into predmety_sp"
                    + " values(PREDMETY_SP_SQ.NEXTVAL,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPlanu);
            ps.setInt(2, idPredmetu);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean pridatPredmetySkupiny(int idSkupiny, int idPredmetu) {
        try {
            qu = "insert into skupiny_predmety"
                    + " values(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idSkupiny);
            ps.setInt(2, idPredmetu);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public ArrayList<Integer> idPredmetu(int id) {
        try {
            ArrayList<Integer> idPred = new ArrayList<>();
            qu = "select id_predmetu from skupiny_predmety where id_skupiny = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPr = result.getInt(1);
                idPred.add(idPr);
            }

            return idPred;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public boolean odstranitKomentar(int id) {
        try {
            qu = "DELETE KOMENTARE WHERE ID_KOMENTARE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public boolean setNullSkupKomentar(int idKom) {
        try {
            qu = "UPDATE SKUPINY SET ID_KOMENTARE = NULL WHERE ID_KOMENTARE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idKom);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public int getSkupKomentar(int idSk) {
        try {
            qu = "SELECT ID_KOMENTARE FROM SKUPINY WHERE ID_SKUPINY = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idSk);
            result = ps.executeQuery();
            result.next();

            int res = result.getInt("id_komentare");
            connection.commit();
            return res;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString() + "Pizdec", Alert.AlertType.ERROR);

        }
        return -1;

    }

    public boolean pridatKomentar(Komentar komentar) {
        try {

            qu = "INSERT INTO KOMENTARE VALUES(?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, komentar.getIdKom().get());
            ps.setTimestamp(2, komentar.getDate().get());
            ps.setString(3, komentar.getObsah().get());
            ps.setString(4, "");
            ps.setInt(5, komentar.getBlokace().get());
            ps.setString(6, komentar.getNazev().get());
            ps.setInt(7, komentar.getIdUzivatele());
            ps.setInt(8, komentar.getIdObrazku());
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatPodKomentar(int idKom, int idPodKom) {
        try {

            qu = "UPDATE KOMENTARE SET ID_PODKOMENTARE = ? WHERE ID_KOMENTARE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idKom);
            ps.setInt(2, idPodKom);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatKomentarSkupine(int idKom, int sk) {
        try {

            qu = "UPDATE SKUPINY SET ID_KOMENTARE = ? WHERE ID_SKUPINY = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idKom);
            ps.setInt(2, sk);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeNazevKomentare(int id, String nazev) {
        try {
            qu = "UPDATE KOMENTARE SET nazev=?  where ID_KOMENTARE=?";
            ps = connection.prepareStatement(qu);
            ps.setString(1, nazev);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean changeObsahKomentare(int id, String obsah) {
        try {
            qu = "UPDATE KOMENTARE SET obsah=?  where ID_KOMENTARE=?";

            ps = connection.prepareStatement(qu);
            ps.setString(1, obsah);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Integer> getPlanPedmetu(int id) {
        try {
            ArrayList<Integer> plany = new ArrayList<>();
            qu = "select psp.id_planu from predmety_sp psp\n"
                    + " inner join  predmety on predmety.id_predmetu = psp.ID_PREDMETU\n"
                    + " where predmety.id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPlanu = result.getInt(1);
                plany.add(idPlanu);
            }
            connection.commit();

            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert("Predmet jeste nema zadny plan.", Alert.AlertType.ERROR);
            return null;
        }

    }

    public ArrayList<Skupina> getAllSkupiny() {
        ArrayList<Skupina> skupiny = new ArrayList<>();
        try {
            qu = "Select * from skupiny";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int idSk = result.getInt(1);
                String nazevSk = result.getString(2);
                int idKomen = result.getInt(3);
                Skupina skupina = new Skupina(idSk, nazevSk, idKomen);
                skupiny.add(skupina);
            }
            connection.commit();

            return skupiny;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public boolean zablokovatUzivatele(int idUziv) {
        try {
            qu = "UPDATE UZIVATELE SET BLOKACE = ? WHERE ID_UZIVATELE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, 0);
            ps.setInt(2, idUziv);
            ps.executeUpdate();

            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public boolean addImage(File file1, Obrazek image) {
        try {
            File file = new File(file1.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);

            ps = connection.prepareStatement("insert into images values (?,?,?,?,?,?,?)");

            ps.setBinaryStream(1, fis);
            ps.setString(2, image.getNazev());
            ps.setInt(3, image.getId());
            ps.setTimestamp(4, image.getcRazitko());
            ps.setString(5, image.getPopisek());
            ps.setInt(6, image.getIdUzivatele());
            ps.setInt(7, image.getDefaultPc());
            ps.executeUpdate();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public List<Obrazek> getImages(int idUziv) {

        try {
            PreparedStatement ps = connection.prepareStatement("select * from images where id_uziv = ?");
            ps.setInt(1, idUziv);
            List<Obrazek> images = new LinkedList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {//now on 1st row

                Blob b = rs.getBlob(1);//2 means 2nd column data
                byte barr[] = b.getBytes(1, (int) b.length());//1 means first image
                String name = rs.getString("name");
                int id = rs.getInt("id");
                Timestamp cRazitko = rs.getTimestamp("cas");
                String popisek = rs.getString("popisek");
                int idUz = rs.getInt("id_uziv");
                int dfPc = rs.getInt("default_pc");
                images.add(new Obrazek(id, name, popisek, new Image(new ByteArrayInputStream(barr)), cRazitko, idUz, dfPc));

            }
            result.close();
            return images;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public int getLike(int id) {

        try {
            ps = connection.prepareStatement("select count(*) from Likes where obrazek_id =" + id);
            result = ps.executeQuery();
            result.next();
            connection.commit();

            return result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public String getNazevObrazku(int id) {

        try {
            ps = connection.prepareStatement("select name from IMAGES where obrazek_id =" + id);
            result = ps.executeQuery();
            result.next();
            connection.commit();

            return result.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getNazevSkupiny(int id) {

        try {
            ps = connection.prepareStatement("select nazev from skupiny where id_skupiny = ?");
            ps.setInt(1, id);
            result = ps.executeQuery();
            result.next();
            connection.commit();

            return result.getString(1);
        } catch (SQLException ex) {
            return "null";
        }

    }

    public void addLike(Like like) {

        try {

            ps = connection.prepareStatement("insert into Likes values (?,?,?,?,?)");

            ps.setInt(1, like.getId());
            ps.setTimestamp(2, like.getcRazitko());
            ps.setInt(3, like.getObr_id());
            ps.setInt(4, like.getPost_id());
            ps.setInt(5, like.getId_uziv());
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean likeCotnrol(int imageId, int idUz) {
        try {
            ps = connection.prepareStatement("Select obrazek_id, id_uzivatele from likes where obrazek_id = ? and id_uzivatele = ?");
            ps.setInt(1, imageId);
            ps.setInt(2, idUz);
            result = ps.executeQuery();
            connection.commit();

            return result.next();
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
        return false;

    }

    public void setProfileImage(int id) {
        try {

            qu = "UPDATE IMAGES SET default_pc = ?  where default_pc = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, 0);
            ps.setInt(2, 1);
            ps.executeUpdate();
            setProf(id);
            connection.commit();

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }

    }

    private boolean setProf(int id) {
        try {
            qu = "UPDATE IMAGES SET default_pc = ?  where id = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, 1);
            ps.setInt(2, id);
            ps.executeUpdate();
            result.close();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
        return false;
    }

    public Obrazek getProfileImage(int id) {

        try {
            Obrazek image = null;
            ps = connection.prepareStatement("select * from images where id_uziv = ? AND default_pc = ?");
            ps.setInt(1, id);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {//now on 1st row

                Blob b = rs.getBlob(1);//2 means 2nd column data
                byte barr[] = b.getBytes(1, (int) b.length());//1 means first image
                String name = rs.getString("name");
                int idObr = rs.getInt("id");
                Timestamp cRazitko = rs.getTimestamp("cas");
                String popisek = rs.getString("popisek");
                int idUz = rs.getInt("id_uziv");
                int dfPc = rs.getInt("default_pc");
                image = new Obrazek(idObr, name, popisek, new Image(new ByteArrayInputStream(barr)), cRazitko, idUz, dfPc);

            }
            return image;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;

        }
    }

    public boolean deletePicture(int id) {
        try {
            qu = "delete from images where id = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public void addOznameniObjekt(ObjektOznameni oo) {

        try {
            ps = connection.prepareStatement("insert into objekt_oznameni values (?,?,?,?,?)");
            ps.setInt(1, oo.getId());
            ps.setInt(2, oo.getEntityTypeId());
            ps.setInt(3, oo.getEntityId());
            ps.setTimestamp(4, oo.getTime());
            ps.setInt(5, oo.getStatus());
            ps.executeUpdate();

            connection.commit();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addOznameni(Oznameni oznameni) {

        try {
            ps = connection.prepareStatement("insert into oznameni values (?,?,?,?)");

            ps.setInt(1, oznameni.getId());
            ps.setInt(2, oznameni.getObjektOznameniId());
            ps.setInt(3, oznameni.getNotifierId());
            ps.setInt(4, oznameni.getStatus());
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addOznameniZmena(OznameniZmena oznameni) {

        try {
            ps = connection.prepareStatement("insert into oznameni_zmena values (?,?,?,?)");

            ps.setInt(1, oznameni.getId());
            ps.setInt(2, oznameni.getObjektOznameniId());
            ps.setInt(3, oznameni.getActorId());
            ps.setInt(4, oznameni.getStatus());
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDatabaseChangeNotification(DatabaseChangeEvent dce) {
        for (QueryChangeDescription qcd : dce.getQueryChangeDescription()) {
            System.out.println(qcd.getQueryId());
            System.out.println(qcd.getQueryChangeEventType().name());
            for (TableChangeDescription tcd : dce.getTableChangeDescription()) {
                System.out.println(tcd.getTableName());
                System.out.println(tcd.getTableOperations());
                System.out.println(Arrays.toString(tcd.getRowChangeDescription()));
            }
        }

    }

//    public ArrayList<ObjektOznameni> getObjektOznameni() throws SQLException {
//        qu = "SELECT * FROM Objr WHERE "
//                + "login=?";
//        ps = connection.prepareStatement(qu);
//        ps.setString(1, login);
//        result = ps.executeQuery();
//        while (result.next()) {
//            int id = result.getInt("id_uzivatele");
//            String login = result.getString("login");
//            String heslo = result.getString("heslo");
//            String jm = result.getString("jmeno");
//            String fm = result.getString("prijmeni");
//            String poznamka = result.getString("poznamka");
//            String email = result.getString("email");
//            Integer blokace = result.getInt("blokace");
//            Uzivatel uzivatel = new Uzivatel(id, login, heslo, jm, fm, poznamka, email, blokace);
//
//        }
//        result.close();
//        connection.commit();
//        return null;
//    }
    public ArrayList<ObjektOznameniZmena> getOznameniById(int id) {
        try {
            ArrayList<ObjektOznameniZmena> oznameniList = new ArrayList<>();

            qu = "Select ob_oznameni.id,ob_oznameni.status,ob_oznameni.entity_id, ozn.notifier_id, ozn_zmena.actor_id ,ent_z.NAZEV,uziv.JMENO,uziv.PRIJMENI"
                    + " from objekt_oznameni ob_oznameni"
                    + " inner join oznameni ozn ON ob_oznameni.id = ozn.notification_object_id"
                    + " inner join oznameni_zmena ozn_zmena ON ozn_zmena.notification_object_id = ob_oznameni.id"
                    + " inner join entita_oznameni ent_z ON ent_z.ID = ob_oznameni.entity_type"
                    + " inner join uzivatele uziv ON ozn_zmena.actor_id = uziv.ID_UZIVATELE"
                    + " where ozn.notifier_id = ? and ob_oznameni.STATUS <> ?"
                    + " order by created_on DESC";

            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, 1);
            result = ps.executeQuery();

            while (result.next()) {

                int idOz = result.getInt("id");
                int idObjOznmStatus = result.getInt("status");
                int entityId = result.getInt("entity_id");
                int notifId = result.getInt("notifier_id");
                int actorId = result.getInt("actor_id");
                String typOznameni = result.getString("nazev");
                String jmeno = result.getString("JMENO");
                String prijmeni = result.getString("PRIJMENI");
                ObjektOznameniZmena ooz = new ObjektOznameniZmena(idOz, idObjOznmStatus, entityId, notifId, actorId, typOznameni, jmeno, prijmeni);
                oznameniList.add(ooz);

            }
            result.close();
            connection.commit();

            return oznameniList;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);

        }
        return null;
    }

    public void prijmoutZadostOPratelstvi(int id1, int id2) {
        try {
            qu = "INSERT INTO FRIENDS VALUES(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.execute();
            result.close();
            connection.commit();

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public void odmitnoutZadostOPratelstvi(int id1) {
        try {
            qu = "DELETE OBJEKT_OZNAMENI WHERE ID = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id1);
            ps.execute();
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public int getCountsOfNotificaion(int id) {
        try {
            qu = "Select count(*)"
                    + " from objekt_oznameni ob_oznameni"
                    + " inner join oznameni ozn ON ob_oznameni.id = ozn.notification_object_id"
                    + " where ozn.notifier_id = ? and ob_oznameni.STATUS <> ?"
                    + " order by created_on DESC";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, 1);
            result = ps.executeQuery();
            result.next();
            int count = result.getInt(1);
            result.close();
            connection.commit();

            return count;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return 0;
        }

    }

    public boolean updateNotifications(int id) {
        try {
            qu = "UPDATE objekt_oznameni SET status=?  where id=?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, 1);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public ArrayList<Uzivatel> getClenoveSkupiny(int id) {
        try {
            ArrayList<Uzivatel> uzivatele = new ArrayList<>();
            qu = "select uz.*from SKUPINA_CLENY  sk_cl\n"
                    + " inner join uzivatele uz on uz.id_uzivatele = sk_cl.id_uzivatele \n"
                    + " where sk_cl.id_skupiny = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                uzivatele.add(uzivatel);
            }
            result.close();
            connection.commit();

            return uzivatele;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean odstranitClenaSkupiny(int idUz, int idSkup) {

        try {
            qu = "DELETE from skupina_cleny"
                    + " where id_uzivatele = ? and id_skupiny = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idUz);
            ps.setInt(2, idSkup);

            ps.executeUpdate();
            result.close();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;

        }
    }

    public boolean odebratVyucujiciho(int idPr, int idVyuc) {
        try {
            qu = "DELETE PREDMETY_VYUCUJICI WHERE ID_VYUCUJICIHO = ? AND ID_PREDMETU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idVyuc);
            ps.setInt(2, idPr);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Uzivatel> getNoveVuycujicicPRedmetu(int id) {
        ArrayList<Uzivatel> users = new ArrayList<>();
        try {
            qu = " SELECT DISTINCT UZ.*  FROM UZIVATELE UZ\n"
                    + " LEFT JOIN PREDMETY_VYUCUJICI PV ON UZ.ID_UZIVATELE = PV.ID_VYUCUJICIHO\n"
                    + " INNER JOIN ROLE_UZIVATELE RUZ ON UZ.ID_UZIVATELE = RUZ.ID_UZIVATELE\n"
                    + " WHERE (PV.ID_PREDMETU <> ? OR PV.ID_PREDMETU IS NULL) AND RUZ.ID_ROLE = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, 77);
            result = ps.executeQuery();
            while (result.next()) {
                Integer idUz = result.getInt("id_uzivatele");
                String loginUz = result.getString("login");
                String hesloUz = result.getString("heslo");
                String jmenoUz = result.getString("jmeno");
                String prijmeniUz = result.getString("prijmeni");
                String poznamkaUz = result.getString("poznamka");
                String email = result.getString("email");
                Integer blokaceUz = result.getInt("blokace");
                Uzivatel uzivatel = new Uzivatel(idUz, loginUz, hesloUz, jmenoUz, prijmeniUz, poznamkaUz, email, blokaceUz);
                users.add(uzivatel);
            }
            result.close();
            connection.commit();

            return users;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public boolean pridatVyucujicihoPredmetu(int idPred, int idVyuc) {
        try {
            qu = "insert into predmety_vyucujici values(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPred);
            ps.setInt(2, idVyuc);
            ps.executeUpdate();
            result.close();
            connection.commit();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<StudijniPlan> getNovePlanyPredmeetu(int id) {
        ArrayList<StudijniPlan> plany = new ArrayList<>();
        try {
            StudijniPlan plan = null;
            qu = "select st.* from STUDIJNI_PLANY st\n"
                    + " left join predmety_sp psp on  st.id_planu= psp.id_planu\n"
                    + " where  psp.id_predmetu <> ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPlanu = result.getInt(1);
                String nazev = result.getString(2);
                int idOboru = result.getInt(3);
                String popis = result.getString(4);
                plan = new StudijniPlan(idPlanu, nazev, idOboru, popis);
                plany.add(plan);
            }
            result.close();
            connection.commit();

            return plany;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPredmetyVyucujiciho(int id) {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            Predmet predmet = null;
            qu = "select * from predmety pr\n"
                    + " inner join predmety_vyucujici pr_vuyc USING(id_predmetu)\n"
                    + " inner join uzivatele uz on uz.id_uzivatele = pr_vuyc.id_vyucujiciho"
                    + " where  pr_vuyc.id_vyucujiciho = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPredmetu = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                predmet = new Predmet(idPredmetu, zkratka, nazev);
                predmety.add(predmet);
            }
            result.close();
            connection.commit();

            return predmety;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public ArrayList<Predmet> getPRedmetyPlanu(int id) {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            Predmet predmet = null;
            qu = "select * from predmety pr\n"
                    + " inner join predmety_sp pr_sp USING(id_predmetu)\n"
                    + " where pr_sp.id_planu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPredmetu = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                predmet = new Predmet(idPredmetu, zkratka, nazev);
                predmety.add(predmet);
            }
            result.close();
            connection.commit();
            return predmety;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public boolean odebratPredmetZPlanu(int idPlanu, int idPredmetu) {
        try {
            qu = "DELETE PREDMETY_SP WHERE ID_PLANU = ? AND ID_PREDMETU = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPlanu);
            ps.setInt(2, idPredmetu);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Predmet> getPredmetyPlan(int id) {
        ArrayList<Predmet> predmety = new ArrayList<>();
        try {
            Predmet predmet = null;
            qu = "select * from predmety pr\n"
                    + " left join predmety_sp pr_sp USING(id_predmetu)\n"
                    + " where pr_sp.id_planu <> ? OR pr_sp.ID_PLANU is null";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPredmetu = result.getInt(1);
                String zkratka = result.getString(2);
                String nazev = result.getString(3);
                predmet = new Predmet(idPredmetu, zkratka, nazev);
                predmety.add(predmet);
            }
            result.close();
            connection.commit();
            return predmety;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public int getIdZapoctu(int idUzivatele, Predmet predmet) {
        try {
            qu = "select id_zapoctu from zapocty where id_predmetu = ? and id_uzivatele = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, predmet.getIdPredmetu());
            ps.setInt(2, idUzivatele);
            result = ps.executeQuery();
            result.next();
            int idZap = result.getInt(1);

            return idZap;
        } catch (SQLException ex) {
            return -1;
        }

    }

    public ArrayList<Podminka> getPodminkyPredmetu(int id) {
        try {
            ArrayList<Podminka> podminky = new ArrayList<>();
            Podminka podminka = null;
            qu = "SELECT podminky_zapocty.* from podminky_predmety\n"
                    + " inner join podminky_zapocty on podminky_zapocty.ID_PODMINKY = podminky_predmety.ID_PODMINKY\n"
                    + " where  podminky_predmety.id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPodminky = result.getInt(1);
                String popis = result.getString(2);
                podminka = new Podminka(idPodminky, popis);
                podminky.add(podminka);
            }
            result.close();
            connection.commit();
            return podminky;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public ArrayList<Podminka> getNovePodminkyPredmetu(int id) {
        try {
            ArrayList<Podminka> podminky = new ArrayList<>();
            Podminka podminka = null;
            qu = "select PODMINKY_ZAPOCTY.* from PODMINKY_ZAPOCTY \n"
                    + " left join PODMINKY_PREDMETY on PODMINKY_PREDMETY.ID_PODMINKY = PODMINKY_ZAPOCTY.id_podminky\n"
                    + " where PODMINKY_PREDMETY.ID_PREDMETU <> ? OR PODMINKY_PREDMETY.ID_PREDMETU is null";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            result = ps.executeQuery();
            while (result.next()) {
                int idPodminky = result.getInt(1);
                String popis = result.getString(2);
                podminka = new Podminka(idPodminky, popis);
                podminky.add(podminka);
            }
            result.close();
            connection.commit();
            return podminky;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }

    }

    public int getStavPodminkyStudenta(int idZap, int idPodminky) {
        try {
            qu = "select status from stav_splneni_podminek where id_zapoctu = ? and id_podminky = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idZap);
            ps.setInt(2, idPodminky);
            result = ps.executeQuery();
            result.next();
            int idPod = result.getInt(1);
            return idPod;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return -1;
        }
    }

    public boolean updateSplneniPodminky(int id, int idPod, int status) {
        try {
            qu = "UPDATE stav_splneni_podminek SET status=?  where id_zapoctu =? and id_podminky = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, status);
            ps.setInt(2, id);
            ps.setInt(3, idPod);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean updateZapocet(int status, int id, int idPred) {
        try {
            qu = "UPDATE zapocty SET stav = ?, casove_razitko = ?  where id_zapoctu =? and id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, status);
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setInt(3, id);
            ps.setInt(4, idPred);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public int getStavZapocet(int id, int idPred) {
        try {
            qu = "Select stav from  zapocty  where id_zapoctu = ? and id_predmetu = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, idPred);
            result = ps.executeQuery();
            result.next();
            return result.getInt(1);
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return -1;
        }
    }

    public boolean addZapocet(Zapocet zapocet, int idPred) {
        try {
            qu = "INSERT INTO ZAPOCTY VALUES(?,?,?,?,?)";
            ps = connection.prepareCall(qu);
            ps.setInt(1, zapocet.getId().get());
            ps.setTimestamp(2, zapocet.getDate().get());
            ps.setInt(3, zapocet.getStav().get());
            ps.setInt(4, idPred);
            ps.setInt(5, zapocet.getIdUzivatele().get());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);

            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }

    }

    public boolean addStavSplneniPodminekPredmetu(Zapocet zapocet, Podminka pod) {
        try {
            qu = "INSERT INTO stav_splneni_podminek VALUES(STAV_SPLNENI_SQ.NEXTVAL,?,?,?)";
            ps = connection.prepareCall(qu);
            ps.setInt(1, 0);
            ps.setInt(2, zapocet.getId().get());
            ps.setInt(3, pod.getIdPodminy().get());
            ps.execute();

            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean deletePodminkuPredmetu(int id, int idPod) {
        try {
            qu = "delete from PODMINKY_PREDMETY where ID_PREDMETU = ? and id_podminky = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, id);
            ps.setInt(2, idPod);
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean pridatPodminkuPredmetu(int idPr, int idPod) {
        try {
            qu = "insert into podminky_predmety values(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPr);
            ps.setInt(2, idPod);
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }
    
    
     public boolean pridatNovouPodminku(Podminka pod) {
        try {
            qu = "insert into podminky_zapocty values(?,?)";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, pod.getIdPodminy().get());
            ps.setString(2, pod.getPopis().get());
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public ArrayList<Podminka> getAllPodminky() {
        ArrayList<Podminka> podminky = new ArrayList<>();
        try {
            qu = "select * from podminky_zapocty";
            ps = connection.prepareStatement(qu);
            result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt(1);
                String nazev = result.getString(2);
                Podminka podminka = new Podminka(id, nazev);
                podminky.add(podminka);
            }
            result.close();

            connection.commit();

            return podminky;

        } catch (SQLException ex) {
            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
    }
    
    
       public boolean deletePodminku( int idPod) {
        try {
            qu = "delete from PODMINKY_ZAPOCTY where id_podminky = ?";
            ps = connection.prepareStatement(qu);
            ps.setInt(1, idPod);
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {

            WindowHelper.showAlert(ex.toString(), Alert.AlertType.ERROR);
            return false;
        }

    }
}
