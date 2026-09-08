import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Random;
import java.util.ArrayList;

// email import
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class Admin extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort;
  private int kID;

  // Ende Attribute
  public Admin(){
    dbVerbinden();
  }

  public Admin(int pID, String pUsername ,  String pVorname, String pName, String pPasswort) {
    super("");
    uID = pID;
    vorname = pVorname;
    name = pName;
    passwort = pPasswort;
    dbVerbinden();
  }

  // Anfang Methoden

  public void dbVerbinden() {
    dbConnector = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
    String fehler = dbConnector.getErrorMessage();
    if (fehler == null) {
      System.out.println("Datenbank wurde erfolgreich verbunden!");
    } else {
      System.out.println("Fehlermeldung: " + fehler);
    }
  }

  public void schuelerHinzufuegen(String pVorname, String pName, String pEmail) {
    if (!checkEmail(pEmail)) {
      // Schritt 1: Klartext-Passwort erzeugen (wird nie gespeichert, nur einmalig angezeigt/gemailt)
      String klartextPasswort = erzeugePasswort();

      // Schritt 2: Passwort hashen, BEVOR irgendetwas in die DB geschrieben wird.
      // Falls die BCrypt-Bibliothek (bcrypt-0_7_0.jar) nicht korrekt als Library
      // eingebunden ist, wirft dieser Aufruf eine NoClassDefFoundError/ExceptionInInitializerError.
      // Wir fangen das hier ab, damit ihr eine klare Fehlermeldung seht statt "es passiert einfach nichts".
      String hash;
      try {
          hash = PasswortUtil.hashPasswort(klartextPasswort);
      } catch (Throwable t) {
          System.err.println("FEHLER beim Hashen des Passworts. Ist bcrypt-0_7_0.jar in BlueJ als Library eingebunden? (Tools -> Preferences -> Libraries)");
          t.printStackTrace();
          return; // Abbruch, kein halb angelegter Nutzer
      }

      // Schritt 3: Nutzer per PreparedStatement einfuegen (schuetzt vor SQL-Injection)
      dbConnector.executeStatement(
          "INSERT INTO nutzer(Vorname, Name, Email, Passwort, Rolle) VALUES(?, ?, ?, ?, 'Schüler')",
          pVorname, pName, pEmail, hash);

      // Schritt 4: Direkt nach jeder kritischen DB-Operation den Fehlerstatus pruefen.
      // getErrorMessage() liefert null, wenn alles geklappt hat - sonst die SQL-Fehlermeldung
      // (z.B. "Unknown column 'Email' in 'field list'", falls die Spalte in der DB fehlt).
      String insertFehler = dbConnector.getErrorMessage();
      if (insertFehler != null) {
          System.err.println("FEHLER beim Einfuegen des Nutzers: " + insertFehler);
          return; // Abbruch, da der Nutzer nicht angelegt wurde
      }

      // Schritt 5: Die eben eingefuegte Zeile wiederfinden, um die neue uID zu bekommen
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname = ? AND Name = ?", pVorname, pName);
      QueryResult r = dbConnector.getCurrentQueryResult();

      // Schritt 6: Absicherung, falls trotz obiger Pruefung nichts gefunden wurde
      if (r == null || r.getRowCount() == 0) {
          System.err.println("FEHLER: Nutzer wurde scheinbar nicht angelegt (SELECT nach INSERT liefert 0 Zeilen).");
          return;
      }

      int id = Integer.parseInt(r.getData()[0][0]);

      System.out.println("Passwort von " + pVorname + " " + pName + ": " + klartextPasswort + " Nutzer ID: " + id);

      String username = erzeugeUsername(id);
      emailSenden(pEmail, username, klartextPasswort);
      Konto konto = new Konto(id);
    } else {
      System.out.println("Da die Email bereits mit einem Konto verknüpft ist, kann kein Nutzer erstellt werden");
    }
  }

  private boolean checkEmail(String email) {
    // Methode liefert true wenn es die Email gibt und false wenn es sie nicht gibt
    dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Email = ?", email);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    return qr != null && qr.getRowCount() > 0;
  }

  public void mensaPersonalHinzufuegen(String pVorname, String pName, String pEmail) {
    if (!checkEmail(pEmail)) {
      String klartextPasswort = erzeugePasswort();

      String hash;
      try {
          hash = PasswortUtil.hashPasswort(klartextPasswort);
      } catch (Throwable t) {
          System.err.println("FEHLER beim Hashen des Passworts. Ist bcrypt-0_7_0.jar in BlueJ als Library eingebunden?");
          t.printStackTrace();
          return;
      }

      dbConnector.executeStatement(
          "INSERT INTO nutzer(Vorname, Name, Email, Passwort, Rolle) VALUES(?, ?, ?, ?, 'Mensa')",
          pVorname, pName, pEmail, hash);

      String insertFehler = dbConnector.getErrorMessage();
      if (insertFehler != null) {
          System.err.println("FEHLER beim Einfuegen des Nutzers: " + insertFehler);
          return;
      }

      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname = ? AND Name = ?", pVorname, pName);
      QueryResult r = dbConnector.getCurrentQueryResult();

      if (r == null || r.getRowCount() == 0) {
          System.err.println("FEHLER: Nutzer wurde scheinbar nicht angelegt (SELECT nach INSERT liefert 0 Zeilen).");
          return;
      }

      int id = Integer.parseInt(r.getData()[0][0]);

      String username = erzeugeUsername(id);
      emailSenden(pEmail, username, klartextPasswort);
      System.out.println("Passwort von " + pVorname + " " + pName + ": " + klartextPasswort + " Nutzer ID: " + id);
    } else {
      System.out.println("Da die Email bereits mit einem Konto verknüpft ist, kann kein Nutzer erstellt werden");
    }
  }

  public void schuelerBearbeiten(int pID, String pName, String pVorname){
    dbConnector.executeStatement("UPDATE nutzer SET Vorname = ?, Name = ? WHERE uID = ?",pName, pVorname, pID);
  }

  public String erzeugePasswort()
  {
      String zeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      Random zufall = new Random();
      String passwort = "";

      for (int i = 0; i < 5; i++)
      {
          int index = zufall.nextInt(zeichen.length());
          passwort += zeichen.charAt(index);
      }

      return passwort;
  }

  public String erzeugeUsername(int uID)
  {
    dbConnector.executeStatement("SELECT Vorname, Name FROM nutzer WHERE uID = ?", uID);
    QueryResult r = dbConnector.getCurrentQueryResult();
    String vorname = r.getData()[0][0];
    String nachname = r.getData()[0][1];
    vorname = vorname.substring(0, 3).toLowerCase();
    nachname = nachname.substring(0, 3).toLowerCase();
    String username = vorname + nachname + Integer.toString(uID);
    dbConnector.executeStatement("UPDATE nutzer SET username = ? WHERE uID = ?", username, uID);
    return username;
  }

  public void schuelerLoeschen(int pID) {
      dbConnector.executeStatement("DELETE FROM nutzer WHERE uID = ?", pID);
      dbConnector.executeStatement("DELETE FROM konto WHERE uID = ?", pID);
  }

  public String getName() {
      return vorname + " " + name;
  }

  public ArrayList<String> getSchueler() {
      ArrayList<String> schueler = new ArrayList();
      dbConnector.executeStatement("SELECT uID, Vorname, Name FROM nutzer WHERE Rolle = 'Schüler' ORDER BY uID ASC");
      QueryResult qr = dbConnector.getCurrentQueryResult();
      for(int x = 0; x < qr.getRowCount(); x++) {
          for(int y = 0; y < qr.getColumnCount(); y++) {
              schueler.add(qr.getData()[x][y]);
          }
      }

      return schueler;
  }

  private void emailSenden(String email, String username, String passwort) {
        // Zugangsdaten kommen aus email.properties statt hartcodiert im Code
        // (siehe EmailConfig.java)
        EmailService emailService = new EmailService(
            EmailConfig.getAbsender(),
            EmailConfig.getAppPasswort()
        );
    
        try {
            emailService.emailSenden(
                email,
                "Sie wurden regestriert",
                "Guten Tag, ein Admin hat für sie ein MensaMaxxing Konto erstellt. \n Nutzername: "+username+" \n Passwort: "+passwort+ " \n Bitte ändern sie das Passwort nach der ersten Anmeldung."
            );
            System.out.println("E-Mail erfolgreich gesendet!");
        } catch (MessagingException e) {
            System.err.println("Fehler beim Senden: " + e.getMessage());
            e.printStackTrace();
        }
    }
  // Ende Methoden

}
