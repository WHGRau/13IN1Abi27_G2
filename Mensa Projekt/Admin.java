import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Random;


public class Admin extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private int uID;
  private String vorname;
  private String name;
  private String passwort;
  private int kID;
  
  // Ende Attribute
  
  public Admin(int pID, String pVorname, String pName, String pPasswort) {
    // Frame-Initialisierunga
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

    public void schuelerHinzufuegen(String pVorname, String pName) {
      String passwort = erzeugePasswort();
      dbConnector.executeStatement("INSERT INTO nutzer(vorname, name, passwort, rolle) VALUES('"+pVorname+"','"+pName+"','"+passwort+"','Schüler')");
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname LIKE '"+pVorname+"' AND Name LIKE '"+pName+"'");
      QueryResult r = dbConnector.getCurrentQueryResult();
      int id = Integer.parseInt(r.getData()[0][0]);
      System.out.println("Passwort von "+ pVorname +" "+ pName + ": " + passwort + " Nutzer ID: " + id);
      Konto konto = new Konto(id);
  }
  
  public void mensaPersonalHinzufuegen(String pVorname, String pName) {
      String passwort = erzeugePasswort();
      dbConnector.executeStatement("INSERT INTO nutzer(vorname, name, passwort, rolle) VALUES('"+pVorname+"','"+pName+"','"+passwort+"','Mensa')");
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname LIKE '"+pVorname+"' AND Name LIKE '"+pName+"'");
      QueryResult r = dbConnector.getCurrentQueryResult();
      int id = Integer.parseInt(r.getData()[0][0]);
      System.out.println("Passwort von "+ pVorname +" "+ pName + ": " + passwort + " Nutzer ID: " + id);
  }
  
  public void schuelerBearbeiten(int pID, String pName, String pVorname){
    dbConnector.executeStatement("UPDATE nutzer SET vorname = '"+pName+"', name = '"+pVorname+"' WHERE uID ='"+pID+"';");
  }
  
  private String erzeugePasswort()
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
  
 
  
  public void schuelerLoeschen(int pID) {
      dbConnector.executeStatement("DELETE FROM nutzer WHERE uID = '"+pID+"';");
      dbConnector.executeStatement("DELETE FROM konto WHERE uID = '"+pID+"';");
  }
  // Ende Methoden

}
