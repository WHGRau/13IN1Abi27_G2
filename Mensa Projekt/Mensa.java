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
    //Überprüfen ob Produkt schon in der DB ist. Wenn ja -> Anzahl erhöhen. Wenn nein -> Produkt aufnehmen
    String sql = ("SELECT pID FROM produkte WHERE Name LIKE '"+pArtikel+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if(qr.getRowCount()==1){
        //dbConnector.executeStatement("UPDATE produkte SET Menge = Menge + "+pAnzahl+" WHERE pID = "+qr.getData()[0][0]);
        System.out.println("Produkt schon vorhanden");
        produktAufnehmen( pArtikel,  pAnzahl);

     }
    else {
       
       dbConnector.executeStatement("INSERT INTO produkte(Name, Preis, Menge, Sollwert,niedrig) VALUES('"+pArtikel+"','"+pPreis+"','"+pAnzahl+"','"+pSoll+"',0 )");
       int a = istNiedrig(pArtikel);
       dbConnector.executeStatement("UPDATE produkte SET niedrig = '"+a+"'WHERE Name LIKE '"+pArtikel+"'");
    }
    
  }
  public int istNiedrig(String pProdukt){
    dbConnector.executeStatement("SELECT menge, Sollwert FROM produkte WHERE Name LIKE '"+pProdukt+"'");
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if(Integer.parseInt(qr.getData()[0][0]) <= Integer.parseInt(qr.getData()[0][1]) * 0.1){
        return 1;
    }
    return 0;
  }   
  
  public void produktLoeschen(String pArtikel){
    dbConnector.executeStatement("DELETE FROM produkte WHERE Name LIKE '"+pArtikel+"'");
  }
  
  public void produktAufnehmen(String pArtikel, int pAnzahl) {
    String sql = ("SELECT pID FROM produkte WHERE Name LIKE '"+pArtikel+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    dbConnector.executeStatement("UPDATE produkte SET Menge = Menge + "+pAnzahl+" WHERE pID = "+qr.getData()[0][0]);
  }
  
  public String verkaufen(String produktName, String chipID, int pMenge) {
      String status = "";
      //Geld vom Konto abziehen
      //user Id von Chip auslesen
      
      dbConnector.executeStatement("SELECT pID FROM produkte WHERE name LIKE '"+produktName+"'");
      QueryResult prod = dbConnector.getCurrentQueryResult();
      int pID= Integer.parseInt(prod.getData()[0][0]);
      
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Chip LIKE '"+chipID+"'");
      QueryResult user = dbConnector.getCurrentQueryResult();
      int uID = Integer.parseInt(user.getData()[0][0]);
      
      //Geld vom Konto abziehen
      
      dbConnector.executeStatement("SELECT preis FROM produkte WHERE pID = "+pID);
      QueryResult qr = dbConnector.getCurrentQueryResult();
      float preis = Float.parseFloat(qr.getData()[0][0]);
      float ges = pMenge * preis;
      
      //Überprüfen ob der Schüler genug auf dem Konto hat
      dbConnector.executeStatement("SELECT kontostand FROM konto WHERE uID = "+ uID);
      qr = dbConnector.getCurrentQueryResult();
      float kontostand = Float.parseFloat(qr.getData()[0][0]);
      
      //Überprüfen ob es genug Artikel gibt
      dbConnector.executeStatement("SELECT Menge FROM produkte WHERE name LIKE '"+produktName+"'");
      qr = dbConnector.getCurrentQueryResult();
      int menge = Integer.parseInt(qr.getData()[0][0]);
      
      if (kontostand >= ges && pMenge <= menge) {
          dbConnector.executeStatement("UPDATE konto SET kontostand = kontostand - "+ges+" WHERE uID ="+uID);
          //Produktmenge verringern
          dbConnector.executeStatement("UPDATE produkte SET Menge = Menge - " +pMenge+ " WHERE name LIKE '"+produktName+"'");
          //In Bestell Tabelle einfügen
          dbConnector.executeStatement("SELECT pId FROM produkte WHERE name LIKE '"+produktName+"'");
          QueryResult ar = dbConnector.getCurrentQueryResult();
          int produktID = Integer.parseInt(ar.getData()[0][0]);
          LocalDateTime datum = LocalDateTime.now();
          String sqlAnweisung = "INSERT INTO bestellung(Wert, Menge, Datum, uID, pID, Typ) VALUES('"+ges+"','"+pMenge+"','"+datum+"','"+uID+"','"+produktID+"', 'Kauf')";
          dbConnector.executeStatement(sqlAnweisung);
          status = "erfolgreich";
      } else {
          if (kontostand < ges){
              status = "kontostand zu niedrig";
          } else if (pMenge > menge){
                  status = "Produkt nicht mehr vorhanden";
          } else {
                  status = "Kontostand und Produkt leer";
              }
          }
      
      return status;
  }
  
  
  public ArrayList<String> statistik(){
      ArrayList<String> rückgabe = new ArrayList<String>();
      dbConnector.executeStatement("SELECT pId , Menge FROM bestellung ORDER BY pId");
      QueryResult r = dbConnector.getCurrentQueryResult();
      int pId = Integer.parseInt(r.getData()[0][0]);
      dbConnector.executeStatement("SELECT Name FROM produkte WHERE pID LIKE '"+pId+"'");
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
            dbConnector.executeStatement("SELECT Name FROM produkte WHERE pID LIKE '"+pId+"'");
            QueryResult nam = dbConnector.getCurrentQueryResult();
            name = nam.getData()[0][0];
        }
      }
      rückgabe.add(name);
      rückgabe.add(Integer.toString(count));
      return rückgabe;
    }
    
   public void geldAufladen(int uID, float pBetrag) {
      LocalDateTime datum = LocalDateTime.now();
      dbConnector.executeStatement("UPDATE konto SET kontostand = kontostand + "+pBetrag+" WHERE uID = "+uID);
      dbConnector.executeStatement("INSERT INTO bestellung(Wert, Menge, Datum, uID, pID, Typ) VALUES('"+pBetrag+"', ' 0', '"+datum+"', '"+uID+"', '0', 'Aufladen')");
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
      dbConnector.executeStatement("UPDATE produkte SET preis = "+pBetrag+" WHERE name = '"+pName+"'");
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
  
  // Ende Methoden
  
}
