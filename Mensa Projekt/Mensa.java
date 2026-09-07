import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mensa extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort;
  private int kID;

  // Ende Attribute
  public Mensa(){dbVerbinden();}
  public Mensa(int pID, String username , String pVorname, String pName, String pPasswort) {
    // Frame-Initialisierung
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

  public void neuesProduktHinzufuegen(String pArtikel, int pAnzahl, double pPreis, int pSoll) {
    dbConnector.executeStatement("SELECT pID FROM produkte WHERE Name = ?", pArtikel);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if (qr.getRowCount() == 1) {
        System.out.println("Produkt schon vorhanden");
        produktAufnehmen(pArtikel, pAnzahl);
    } else {
        dbConnector.executeStatement(
            "INSERT INTO produkte(Name, Preis, Menge, Sollwert, niedrig) VALUES(?, ?, ?, ?, 0)",
            pArtikel, pPreis, pAnzahl, pSoll);
        int a = istNiedrig(pArtikel);
        dbConnector.executeStatement("UPDATE produkte SET niedrig = ? WHERE Name = ?", a, pArtikel);
    }
  }

  public int istNiedrig(String pProdukt){
    dbConnector.executeStatement("SELECT Menge, Sollwert FROM produkte WHERE Name = ?", pProdukt);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if (Integer.parseInt(qr.getData()[0][0]) <= Integer.parseInt(qr.getData()[0][1]) * 0.1) {
        return 1;
    }
    return 0;
  }

  public void produktLoeschen(String pArtikel){
    dbConnector.executeStatement("DELETE FROM produkte WHERE Name = ?", pArtikel);
  }

  public void produktAufnehmen(String pArtikel, int pAnzahl) {
    dbConnector.executeStatement("SELECT pID FROM produkte WHERE Name = ?", pArtikel);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    dbConnector.executeStatement(
        "UPDATE produkte SET Menge = Menge + ? WHERE pID = ?",
        pAnzahl, Integer.parseInt(qr.getData()[0][0]));
  }

  /**
   * Verkauft pMenge Einheiten von produktName an den Nutzer, dessen Chip-ID
   * chipID entspricht. Liefert einen Status-String zurueck ("erfolgreich",
   * "kontostand zu niedrig", "Produkt nicht mehr vorhanden" oder
   * "Kontostand und Produkt leer").
   */
  public String verkaufen(String produktName, String chipID, int pMenge) {
      String status = "";

      dbConnector.executeStatement("SELECT pID FROM produkte WHERE Name = ?", produktName);
      QueryResult prod = dbConnector.getCurrentQueryResult();
      int pID = Integer.parseInt(prod.getData()[0][0]);

      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Chip = ?", chipID);
      QueryResult user = dbConnector.getCurrentQueryResult();
      int uID = Integer.parseInt(user.getData()[0][0]);

      dbConnector.executeStatement("SELECT Preis FROM produkte WHERE pID = ?", pID);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      float preis = Float.parseFloat(qr.getData()[0][0]);
      float ges = pMenge * preis;

      dbConnector.executeStatement("SELECT Kontostand FROM konto WHERE uID = ?", uID);
      qr = dbConnector.getCurrentQueryResult();
      float kontostand = Float.parseFloat(qr.getData()[0][0]);

      dbConnector.executeStatement("SELECT Menge FROM produkte WHERE Name = ?", produktName);
      qr = dbConnector.getCurrentQueryResult();
      int menge = Integer.parseInt(qr.getData()[0][0]);

      if (kontostand >= ges && pMenge <= menge) {
          dbConnector.executeStatement(
              "UPDATE konto SET Kontostand = Kontostand - ? WHERE uID = ?", ges, uID);
          dbConnector.executeStatement(
              "UPDATE produkte SET Menge = Menge - ? WHERE Name = ?", pMenge, produktName);

          dbConnector.executeStatement("SELECT pID FROM produkte WHERE Name = ?", produktName);
          QueryResult ar = dbConnector.getCurrentQueryResult();
          int produktID = Integer.parseInt(ar.getData()[0][0]);

          LocalDateTime datum = LocalDateTime.now();
          dbConnector.executeStatement(
              "INSERT INTO bestellung(Wert, Menge, Datum, uID, pID, Typ) VALUES(?, ?, ?, ?, ?, 'Kauf')",
              ges, pMenge, datum, uID, produktID);
          status = "erfolgreich";
      } else {
          if (kontostand < ges) {
              status = "kontostand zu niedrig";
          } else if (pMenge > menge) {
              status = "Produkt nicht mehr vorhanden";
          } else {
              status = "Kontostand und Produkt leer";
          }
      }

      return status;
  }

  public ArrayList<String> statistik(){
      ArrayList<String> rückgabe = new ArrayList<String>();
      dbConnector.executeStatement("SELECT pID, Menge FROM bestellung WHERE Typ = 'Kauf' ORDER BY pID");
      QueryResult r = dbConnector.getCurrentQueryResult();
      int pId = Integer.parseInt(r.getData()[0][0]);
      dbConnector.executeStatement("SELECT Name FROM produkte WHERE pID = ?", pId);
      QueryResult na = dbConnector.getCurrentQueryResult();
      String name = na.getData()[0][0];
      int count = 0;
      for(int i = 0; i< r.getRowCount(); i++){
        if(pId == Integer.parseInt(r.getData()[i][0]))
            count = count + Integer.parseInt(r.getData()[i][1]);
        else{
            rückgabe.add(name);
            rückgabe.add(Integer.toString(count));
            pId = Integer.parseInt(r.getData()[i][0]);
            count = 0 ;
            count = count + Integer.parseInt(r.getData()[i][1]);
            dbConnector.executeStatement("SELECT Name FROM produkte WHERE pID = ?", pId);
            QueryResult nam = dbConnector.getCurrentQueryResult();
            name = nam.getData()[0][0];
        }
      }
      rückgabe.add(name);
      rückgabe.add(Integer.toString(count));
      return rückgabe;
  }

  /**
   * Ladet das Konto des Nutzers auf, der zur angegebenen Chip-ID gehoert.
   */
  public void geldAufladen(String chipID, float pBetrag) {
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Chip = ?", chipID);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      int uID = Integer.parseInt(qr.getData()[0][0]);

      LocalDateTime datum = LocalDateTime.now();
      dbConnector.executeStatement("UPDATE konto SET Kontostand = Kontostand + ? WHERE uID = ?", pBetrag, uID);
      dbConnector.executeStatement(
          "INSERT INTO bestellung(Wert, Menge, Datum, uID, pID, Typ) VALUES(?, 0, ?, ?, 0, 'Aufladen')",
          pBetrag, datum, uID);
  }

  public ArrayList<String> getLager() {
      ArrayList<String> lager = new ArrayList();
      dbConnector.executeStatement("SELECT Name, Menge, Preis FROM produkte");
      QueryResult qr = dbConnector.getCurrentQueryResult();
      for(int x = 0; x < qr.getRowCount(); x++) {
          for(int y = 0; y < qr.getColumnCount(); y++) {
              lager.add(qr.getData()[x][y]);
          }
      }
      return lager;
  }

  public void preisaendern(float pBetrag , String pName){
      dbConnector.executeStatement("UPDATE produkte SET Preis = ? WHERE Name = ?", pBetrag, pName);
  }

  public ArrayList<String> bestandMesser(){
    ArrayList<String> rückgabe = new ArrayList<String>();
    dbConnector.executeStatement("SELECT Name FROM produkte");
    QueryResult r = dbConnector.getCurrentQueryResult();

    if(r == null){
        return rückgabe;
    }

    for(int i = 0; i < r.getRowCount(); i++){
        String name = r.getData()[i][0];
        if(istNiedrig(name) == 1){
            rückgabe.add(name);
        }
    }

    return rückgabe;
  }

  public ArrayList<String> getVerlauf() {
      ArrayList<String> verlauf = new ArrayList<>();
      dbConnector.executeStatement(
          "SELECT bestellung.Datum, bestellung.Typ, produkte.Name, bestellung.Menge, nutzer.username " +
          "FROM bestellung JOIN nutzer ON bestellung.uID = nutzer.uID " +
          "LEFT JOIN produkte ON bestellung.pID = produkte.pID");
      QueryResult qr = dbConnector.getCurrentQueryResult();

      String[][] data = qr.getData();

      for (int x = 0; x < qr.getRowCount(); x++) {
          for (int y = 0; y < qr.getColumnCount(); y++) {
              if (data[x][y] == null) {
                  verlauf.add("--");
              } else {
                  verlauf.add(data[x][y]);
              }
          }
      }

      return verlauf;
  }

  // Ende Methoden

}
