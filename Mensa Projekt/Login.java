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

    dbConnector.executeStatement(
        "SELECT uID, Vorname, Name, Passwort, Rolle FROM nutzer WHERE username = ?",
        username);
    QueryResult r = dbConnector.getCurrentQueryResult();

    if (r == null || r.getRowCount() != 1) {
      System.out.println("Anmeldedaten falsch!");
      return null;
    }
    
    int uID = Integer.parseInt(r.getData()[0][0]);
    String vorname = r.getData()[0][1];
    String name = r.getData()[0][2];
    String gespeicherterHash = r.getData()[0][3];
    String rolle = r.getData()[0][4];
    
    if (!PasswortUtil.pruefePasswort(pPasswort, gespeicherterHash)) {
      System.out.println("Anmeldedaten falsch!");
      return null;
    }

    System.out.println("Anmeldedaten richtig!");
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

  public void passwortzuruck(String pPasswort, String pEmail) {
    String hash = PasswortUtil.hashPasswort(pPasswort);

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

  public boolean checkEmail(String email) {
    //True wenn es email gibt
      
    dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Email = ?", email);
    QueryResult qr = dbConnector.getCurrentQueryResult();
   
    return qr != null && qr.getRowCount() > 0;
  }

  public void resetPasswort(String email) {
    if (checkEmail(email)) {
      // uID zur E-Mail-Adresse ermitteln
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Email = ?", email);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      int uID = Integer.parseInt(qr.getData()[0][0]);

      String passwortNeu = erzeugePasswort();

      String hash = PasswortUtil.hashPasswort(passwortNeu);

    
      dbConnector.executeStatement("UPDATE nutzer SET Passwort = ? WHERE uID = ?", hash, uID);

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
        EmailService emailService = new EmailService(
            "mensamaxxing@gmail.com",        
            "jspv nbmu iwxr jpxi"           
        );

        try {
            emailService.emailSenden(
                email,
                "Ihr Mensa Passwort wurde zurückgesetzt",
                "Guten Tag, Ihr MensaMaxxing Passwort wurde zurückgetzt. Ihr neues Passwort lautet: "+passwort+ " \n Bitte ändern sie es beim nächsten Anmelden zu einem von ihnen gewählten Passwort."
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



