import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Nutzer extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort;
  private int kID;
  private Konto konto;
  
  // Ende Attribute
  
  public Nutzer(int pID, String pVorname, String pName, String pPasswort) {
    // Frame-Initialisierung
    super("");
    uID = pID;
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

  public void passwortBearbeiten(String pPasswort, String pAltesPasswort){
    String sql = ("SELECT passwort FROM nutzer WHERE uID LIKE '"+uID+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if(qr.getRowCount()==1){
        if (pAltesPasswort.equals(qr.getData()[0][0])){
            dbConnector.executeStatement("UPDATE nutzer SET Passwort = '"+pPasswort+"' WHERE uID ='"+uID+"';");
        }
        else {
            System.out.println("Nutzer nicht gefunden oder falsches Passwort");
        }
    }
   else {
       System.out.println("Nutzer nicht gefunden oder falsches Passwort");
   }
 }
 
  public void kontoPinBearbeiten(int pAlterPin, int pNeuerPin) {
    String sql = ("SELECT pin FROM konto WHERE uID LIKE '"+uID+"'");
    dbConnector.executeStatement(sql);
    QueryResult qr = dbConnector.getCurrentQueryResult();
    if(qr.getRowCount()==1){
        if (pAlterPin == Integer.parseInt(qr.getData()[0][0])){
            dbConnector.executeStatement("UPDATE konto SET pin = '"+pNeuerPin+"' WHERE uID ='"+uID+"';");
        }
        else {
            System.out.println("Konto nicht gefunden oder falsches Passwort");
        }
    }
    else {
       System.out.println("Konto nicht gefunden oder falsches Passwort");
    }
  }
  // Ende Methoden
}
