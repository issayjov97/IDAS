package Files;

import database.DatabaseHelper;
import database.PasswordGenerator;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sempraceidas2.mainwindow.WindowHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author dzhohar
 */
public class CSVParser {

    public static boolean importPredmety() throws IOException {
        BufferedReader fileReader = null;
        System.out.println("user.dir");
        try {
            String line = "src/Files/predmety.csv";
            fileReader = new BufferedReader(new FileReader(line));
            Connection connection = DatabaseHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement("merge into predmety using (select 1 from dual)\n"
                    + " ON (predmety.ZKRATKA LIKE ? AND predmety.NAZEV LIKE ?)\n"
                    + " WHEN NOT MATCHED THEN \n"
                    + " INSERT VALUES(PREDMETY_SQ.NEXTVAL,?,?)");
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(";");
                String tk1 = tokens[3].replace("\"", "");
                String tk2 = tokens[5].replace("\"", "");
                statement.setString(1, "%" + tk1 + "%");
                statement.setString(2, "%" + tk2 + "%");
                statement.setString(3, tk1);
                statement.setString(4, tk2);
                statement.execute();
            }
        } catch (IOException | SQLException e) {
        } finally {
                fileReader.close();
        }

        return true;
    }

    public static boolean importUcitele() throws IOException {
        BufferedReader fileReader = null;
        System.out.println("user.dir");
        try {
            String line = "src/Files/ucitele.csv";
            fileReader = new BufferedReader(new FileReader(line));
            Connection connection = DatabaseHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement("merge into uzivatele using (select 1 from dual)\n"
                    + " ON (uzivatele.id_uzivatele = ?)\n"
                    + " WHEN NOT MATCHED THEN \n"
                    + " INSERT VALUES(?,?,?,?,?,?,?,?)");
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(";");
                String id = tokens[0].replace("\"", "").trim();
                String jmeno = tokens[1].replace("\"", "");
                String prijmeni = tokens[2].replace("\"", "");
                String email = tokens[9].replace("\"", "").trim();
                String login = "vuyc" + id;
                String heslo = PasswordGenerator.generatePassword(10, PasswordGenerator.ALPHA);
                if (email.length() <=0) {
                    email = "vuyc" + id + "@vyucupce.cz";
                }
                statement.setInt(1, Integer.valueOf(id));
                statement.setString(2, id);
                statement.setString(3, login);
                statement.setString(4, heslo);
                statement.setString(5, jmeno);
                statement.setString(6, prijmeni);
                statement.setString(7, "");
                statement.setString(8, email);
                statement.setInt(9, 1);
                statement.execute();

            }
        } catch (IOException | SQLException e) {
        } finally {
            fileReader.close();
        }

        return true;
    }
}
