import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Mensa extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort;
  private int kID;
  // Ende Attribute
  
  public Mensa(int pID, String pVorname, String pName, String pPasswort) {
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
  
  public void produktAufnehmen(String pArtikel, int pAnzahl, float pPreis) {
    //Überprüfen ob Produkt schon in der DB ist. Wenn ja -> Anzahl erhöhen. Wenn nein -> Produkt aufnehmen
    String sql = ("SELECT pID FROM produkte WHERE Name LIKE '"+pArtikel+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if(qr.getRowCount()==1){
        dbConnector.executeStatement("UPDATE produkte SET Menge = Menge + "+pAnzahl+" WHERE pID = "+qr.getData()[0][0]);
        
     }
    else {
       
       dbConnector.executeStatement("INSERT INTO produkte(Name, Preis, Menge) VALUES('"+pArtikel+"','"+pPreis+"','"+pAnzahl+"')");
    }
      
  }
  
  public void produktAufnehmen(String pArtikel, int pAnzahl) {
    String sql = ("SELECT pID FROM produkte WHERE Name LIKE '"+pArtikel+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    dbConnector.executeStatement("UPDATE produkte SET Menge = Menge + "+pAnzahl+" WHERE pID = "+qr.getData()[0][0]);
  }
  
  public void verkaufen(int produktID, int schuelerID, int pMenge) {
      //Geld vom Konto abziehen
      dbConnector.executeStatement("SELECT preis FROM produkte WHERE pID LIKE "+produktID);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      float preis = Float.parseFloat(qr.getData()[0][0]);
      float ges = pMenge * preis;
      
      //Überprüfen ob der Schüler genug auf dem Konto hat
      dbConnector.executeStatement("SELECT kontostand FROM konto WHERE uID = "+ schuelerID);
      qr = dbConnector.getCurrentQueryResult();
      float kontostand = Float.parseFloat(qr.getData()[0][0]);
      
      //Überprüfen ob es genug Artikel gibt
      dbConnector.executeStatement("SELECT Menge FROM produkte WHERE pID = "+ produktID);
      qr = dbConnector.getCurrentQueryResult();
      int menge = Integer.parseInt(qr.getData()[0][0]);
      
      if (kontostand >= ges && pMenge < menge) {
          dbConnector.executeStatement("UPDATE konto SET kontostand = kontostand - "+ges+" WHERE uID ="+schuelerID);
          //Produktmenge verringern
          dbConnector.executeStatement("UPDATE produkte SET Menge = Menge - " +pMenge+ " WHERE pID = " + produktID);
          //In Bestell Tabelle einfügen
          LocalDateTime datum = LocalDateTime.now();
          dbConnector.executeStatement("INSERT INTO bestellung(Wert, Menge, Datum, uID, pID) VALUES('"+ges+"','"+pMenge+"','"+datum+"','"+schuelerID+"','"+produktID+"')");
      } else {
          System.out.println("Kontostand zu niedrig oder zu Bestand zu niedrig");
      }
  }
  
  public void geldAufladen(int uID, float pBetrag) {
      dbConnector.executeStatement("UPDATE konto SET kontostand = kontostand + "+pBetrag+" WHERE uID = "+uID);
  }
  
  // Ende Methoden
  
}
