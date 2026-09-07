import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.ArrayList;


public class Nutzer extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort; // enthaelt den BCrypt-Hash, nicht das Klartext-Passwort
  private String username;
  private int kID;
  private Konto konto;

  // Ende Attribute

  public Nutzer(int pID , String pUsername, String pVorname, String pName, String pPasswort) {
    super("");
    uID = pID;
    username = pUsername;
    vorname = pVorname;
    name = pName;
    passwort = pPasswort;
    konto = new Konto(pID);
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

  /**
   * Aendert das Passwort, nachdem das alte Passwort erfolgreich geprueft wurde.
   * Das neue Passwort wird vor dem Speichern mit BCrypt gehasht.
   */
  public void passwortBearbeiten(String pPasswort, String pAltesPasswort){
    dbConnector.executeStatement("SELECT Passwort FROM nutzer WHERE uID = ?", uID);
    QueryResult qr = dbConnector.getCurrentQueryResult();

    if (qr != null && qr.getRowCount() == 1) {
      String gespeicherterHash = qr.getData()[0][0];
      if (PasswortUtil.pruefePasswort(pAltesPasswort, gespeicherterHash)) {
        String neuerHash = PasswortUtil.hashPasswort(pPasswort);
        dbConnector.executeStatement("UPDATE nutzer SET Passwort = ? WHERE uID = ?", neuerHash, uID);
        passwort = neuerHash;
      } else {
        System.out.println("Nutzer nicht gefunden oder falsches Passwort");
      }
    } else {
      System.out.println("Nutzer nicht gefunden oder falsches Passwort");
    }
  }

  public void kontoPinBearbeiten(int pAlterPin, int pNeuerPin) {
    dbConnector.executeStatement("SELECT Pin FROM konto WHERE uID = ?", uID);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if (qr != null && qr.getRowCount() == 1) {
      if (pAlterPin == Integer.parseInt(qr.getData()[0][0])) {
        dbConnector.executeStatement("UPDATE konto SET Pin = ? WHERE uID = ?", pNeuerPin, uID);
      } else {
        System.out.println("Konto nicht gefunden oder falsches Passwort");
      }
    } else {
      System.out.println("Konto nicht gefunden oder falsches Passwort");
    }
  }

  /**
   * Prueft ein eingegebenes Klartext-Passwort gegen den in der DB gespeicherten
   * BCrypt-Hash.
   */
  public boolean checkPasswort(String pPasswort) {
    dbConnector.executeStatement("SELECT Passwort FROM nutzer WHERE uID = ?", uID);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if (qr != null && qr.getRowCount() == 1) {
      String gespeicherterHash = qr.getData()[0][0];
      return PasswortUtil.pruefePasswort(pPasswort, gespeicherterHash);
    }
    return false;
  }

  public int getID() {
      return uID;
  }

  public String getName() {
      return vorname + " " + name;
  }

  public String getKontostand() {
    dbConnector.executeStatement("SELECT Kontostand FROM konto WHERE uID = ?", uID);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    String kontostand = qr.getData()[0][0];
    return kontostand + " €";
  }

  public ArrayList<String> getKaeufe() {
      ArrayList<String> kaeufe = new ArrayList();
      dbConnector.executeStatement(
          "SELECT bestellung.Datum, produkte.Name, bestellung.Menge, bestellung.Wert, bestellung.Typ " +
          "FROM produkte, bestellung " +
          "WHERE bestellung.uID = ? AND bestellung.pID = produkte.pID ORDER BY bestellung.Datum DESC",
          uID);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      for(int x = 0; x < qr.getRowCount(); x++) {
          for(int y = 0; y < qr.getColumnCount(); y++) {
              kaeufe.add(qr.getData()[x][y]);
          }
      }

      return kaeufe;
  }
  // Ende Methoden
}
