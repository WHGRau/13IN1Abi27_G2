import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Random;

// email import
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class Login extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private Nutzer aktSchueler;
  private Admin aktAdmin;
  private Mensa aktMensa;

  // Ende Attribute

  public Login() {
    // Frame-Initialisierung
    super("");
    dbVerbinden();
  }

  // Anfang Methoden

  /**
   * Prueft Username + Passwort und liefert je nach Rolle ein Nutzer-,
   * Admin- oder Mensa-Objekt zurueck, oder null bei falschen Anmeldedaten.
   */
  public Object login(String username, String pPasswort) {
    dbVerbinden();

    // SCHRITT 1: Alle benoetigten Daten in EINER einzigen Abfrage holen,
    // statt (wie vorher) vier separate Abfragen zu machen. Das ist nicht nur
    // effizienter, sondern auch sicherer: Je weniger Einzelabfragen mit
    // Nutzereingaben, desto weniger Angriffsflaeche.
    // WICHTIG: Der "?" ist ein Platzhalter. Der Wert von "username" wird
    // NICHT in den SQL-String eingebaut, sondern separat als Parameter
    // uebergeben (siehe DatabaseConnector.executeStatement(String, Object...)).
    // Das schuetzt vor SQL-Injection, egal was im Username-Feld steht.
    dbConnector.executeStatement(
        "SELECT uID, Vorname, Name, Passwort, Rolle FROM nutzer WHERE username = ?",
        username);
    QueryResult r = dbConnector.getCurrentQueryResult();

    // SCHRITT 2: Pruefen, ob GENAU EIN Nutzer mit diesem Username gefunden wurde.
    // r == null kann passieren, wenn die Abfrage selbst fehlgeschlagen ist
    // (z.B. Tippfehler im Spaltennamen). getRowCount() != 1 deckt sowohl
    // "kein solcher Nutzer" (0 Zeilen) als auch unerwartete Duplikate ab.
    // Das verhindert auch den alten Absturz (ArrayIndexOutOfBoundsException),
    // der passierte, wenn direkt auf r.getData()[0][0] zugegriffen wurde,
    // ohne vorher zu pruefen, ob ueberhaupt eine Zeile da ist.
    if (r == null || r.getRowCount() != 1) {
      System.out.println("Anmeldedaten falsch!");
      return null;
    }

    // SCHRITT 3: Werte aus der Ergebniszeile auslesen.
    // r.getData()[0] ist die erste (und einzige) Ergebniszeile,
    // [0..4] sind die Spalten in der Reihenfolge des SELECT.
    int uID = Integer.parseInt(r.getData()[0][0]);
    String vorname = r.getData()[0][1];
    String name = r.getData()[0][2];
    String gespeicherterHash = r.getData()[0][3]; // das ist der BCrypt-Hash aus der DB, NICHT das Klartext-Passwort
    String rolle = r.getData()[0][4];

    // SCHRITT 4: Passwort-Pruefung ueber BCrypt statt .equals().
    // pPasswort ist das, was der Nutzer eingetippt hat (Klartext).
    // gespeicherterHash ist der BCrypt-Hash-String aus der Datenbank.
    // PasswortUtil.pruefePasswort() hasht das eingegebene Passwort NICHT
    // manuell, sondern nutzt intern BCrypt.verifyer(), der den Salt aus dem
    // gespeicherten Hash extrahiert und korrekt vergleicht.
    if (!PasswortUtil.pruefePasswort(pPasswort, gespeicherterHash)) {
      System.out.println("Anmeldedaten falsch!");
      return null;
    }

    System.out.println("Anmeldedaten richtig!");

    // SCHRITT 5: Je nach Rolle das passende Objekt erzeugen und zurueckgeben.
    // Beachte: Wir geben gespeicherterHash weiter (nicht das Klartext-Passwort!),
    // weil Nutzer/Admin/Mensa diesen Hash intern speichern, um ihn spaeter
    // z.B. bei "Passwort aendern" gegen das alte Passwort zu pruefen.
    switch (rolle) {
      case "Schüler":
        System.out.println("Schüler");
        aktSchueler = erstelleSchueler(uID, username, vorname, name, gespeicherterHash);
        return aktSchueler;
      case "Admin":
        System.out.println("Admin");
        aktAdmin = erstelleAdmin(uID, username, vorname, name, gespeicherterHash);
        return aktAdmin;
      case "Mensa":
        System.out.println("Mensa");
        aktMensa = erstelleMensa(uID, username, vorname, name, gespeicherterHash);
        return aktMensa;
      default:
        System.out.println("Du hast keine Berechtigung!");
        return null;
    }
  }

  /**
   * Setzt das Passwort eines Nutzers anhand seiner E-Mail-Adresse neu.
   * WICHTIG: pPasswort wird hier NICHT im Klartext gespeichert, sondern
   * zuerst gehasht (Schritt 1), dann wird nur der Hash in die DB geschrieben.
   */
  public void passwortzuruck(String pPasswort, String pEmail) {
    // SCHRITT 1: Klartext-Passwort hashen, BEVOR es in die DB geschrieben wird.
    String hash = PasswortUtil.hashPasswort(pPasswort);

    // SCHRITT 2: PreparedStatement statt String-Verkettung -> kein SQL-Injection-Risiko,
    // egal was in pEmail steht.
    dbConnector.executeStatement("UPDATE nutzer SET Passwort = ? WHERE Email = ?", hash, pEmail);
  }

  public void dbVerbinden() {
    dbConnector = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
    String fehler = dbConnector.getErrorMessage();
    if (fehler == null) {
      System.out.println("Datenbank wurde erfolgreich verbunden!");
    } else {
      System.out.println("Fehlermeldung: " + fehler);
    }
  }

  private Nutzer erstelleSchueler(int uID, String username, String pVorname, String pNachname, String pPasswortHash) {
    return new Nutzer(uID, username, pVorname, pNachname, pPasswortHash);
  }

  private Admin erstelleAdmin(int uID, String username, String pVorname, String pName, String pPasswortHash) {
    return new Admin(uID, username, pVorname, pName, pPasswortHash);
  }

  private Mensa erstelleMensa(int uID, String username, String pVorname, String pName, String pPasswortHash) {
    return new Mensa(uID, username, pVorname, pName, pPasswortHash);
  }

  /**
   * Liefert true, wenn es einen Nutzer mit dieser E-Mail-Adresse gibt.
   * PreparedStatement statt String-Verkettung.
   */
  public boolean checkEmail(String email) {
    dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Email = ?", email);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    // qr kann null sein, wenn die Abfrage fehlschlug (z.B. Spalte Email fehlt in der DB!)
    return qr != null && qr.getRowCount() > 0;
  }

  /**
   * Erzeugt ein neues Zufallspasswort fuer den Nutzer mit der angegebenen
   * E-Mail-Adresse, speichert dessen HASH (nicht das Klartext-Passwort!)
   * in der DB und verschickt das neue Klartext-Passwort per E-Mail
   * (einmalig, Nutzer sollte es nach dem Einloggen aendern).
   */
  public void resetPasswort(String email) {
    if (checkEmail(email)) {
      // uID zur E-Mail-Adresse ermitteln
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Email = ?", email);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      int uID = Integer.parseInt(qr.getData()[0][0]);

      // SCHRITT 1: neues Zufallspasswort im Klartext erzeugen
      String passwortNeu = erzeugePasswort();

      // SCHRITT 2: das Klartext-Passwort HASHEN, bevor es gespeichert wird
      String hash = PasswortUtil.hashPasswort(passwortNeu);

      // SCHRITT 3: nur den Hash in die Datenbank schreiben (PreparedStatement)
      dbConnector.executeStatement("UPDATE nutzer SET Passwort = ? WHERE uID = ?", hash, uID);

      // SCHRITT 4: das KLARTEXT-Passwort per E-Mail an den Nutzer schicken,
      // damit er sich einmalig damit einloggen kann. Das ist der einzige
      // Moment, an dem das Klartext-Passwort noch existiert - danach ist
      // nur noch der Hash in der DB gespeichert.
      emailSenden(email, passwortNeu);
    }
  }

  private String erzeugePasswort() {
    String zeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random zufall = new Random();
    String passwort = "";

    for (int i = 0; i < 5; i++) {
      int index = zufall.nextInt(zeichen.length());
      passwort += zeichen.charAt(index);
    }

    return passwort;
  }

  private void emailSenden(String email, String passwort) {
    // Zugangsdaten kommen aus email.properties statt hartcodiert im Code
    // (siehe EmailConfig.java) - dasselbe Prinzip wie in Admin.java
    EmailService emailService = new EmailService(
        EmailConfig.getAbsender(),
        EmailConfig.getAppPasswort()
    );

    try {
      emailService.emailSenden(
          email,
          "Ihr Mensa Passwort wurde zurückgesetzt",
          "Guten Tag, Ihr MensaMaxxing Passwort wurde zurückgesetzt. Ihr neues Passwort lautet: " + passwort
              + " \n Bitte ändern sie es beim nächsten Anmelden zu einem von ihnen gewählten Passwort."
      );
      System.out.println("E-Mail erfolgreich gesendet!");
    } catch (MessagingException e) {
      System.err.println("Fehler beim Senden: " + e.getMessage());
      e.printStackTrace();
    }
  }
  // Ende Methoden

  public static void main(String[] args) {
    new Login();
  }
}
