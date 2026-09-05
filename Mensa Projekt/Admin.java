import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Random;
import java.util.ArrayList;

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
    dbVerbinden();}
  public Admin(int pID, String pUsername ,  String pVorname, String pName, String pPasswort) {
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

    public void schuelerHinzufuegen(String pVorname, String pName , String pGmail) {
      System.out.println("hinzufügen start");
      String passwort = erzeugePasswort();
      dbConnector.executeStatement("SELECT email FROM nutzer WHERE email LIKE '"+pGmail+"'");
      QueryResult qr = dbConnector.getCurrentQueryResult();
      if (qr.getRowCount() == 0){
          System.out.println("hinzufügen 1");
          dbConnector.executeStatement("INSERT INTO nutzer(vorname, name, passwort, rolle, email) VALUES('"+pVorname+"','"+pName+"','"+passwort+"','Schüler','"+pGmail+"')");
          System.out.println("hinzufügen 2");
          dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname LIKE '"+pVorname+"' AND Name LIKE '"+pName+"'");
          System.out.println("hinzufügen 3");
          QueryResult r = dbConnector.getCurrentQueryResult();
          int id = Integer.parseInt(r.getData()[0][0]);
          System.out.println("Passwort von "+ pVorname +" "+ pName + ": " + passwort + " Nutzer ID: " + id);
          erzeugeUsername(id);
          Konto konto = new Konto(id);
      }
      else System.out.println("w mail schon vorhanden");
  }
  
  public void mensaPersonalHinzufuegen(String pVorname, String pName) {
      String passwort = erzeugePasswort();
      dbConnector.executeStatement("INSERT INTO nutzer(vorname, name, passwort, rolle) VALUES('"+pVorname+"','"+pName+"','"+passwort+"','Mensa')");
      dbConnector.executeStatement("SELECT uID FROM nutzer WHERE Vorname LIKE '"+pVorname+"' AND Name LIKE '"+pName+"'");
      QueryResult r = dbConnector.getCurrentQueryResult();
      int id = Integer.parseInt(r.getData()[0][0]);
      erzeugeUsername(id);
      System.out.println("Passwort von "+ pVorname +" "+ pName + ": " + passwort + " Nutzer ID: " + id);
  }
  
  public void schuelerBearbeiten(int pID, String pName, String pVorname){
    dbConnector.executeStatement("UPDATE nutzer SET vorname = '"+pName+"', name = '"+pVorname+"' WHERE uID ='"+pID+"';");
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
    
    
    public void erzeugeUsername(int uID)
    {
        
      dbConnector.executeStatement("SELECT vorname, name FROM Nutzer WHERE uID LIKE '"+uID+"'");
      QueryResult r = dbConnector.getCurrentQueryResult(); 
      String vorname = r.getData()[0][0];
      String nachname = r.getData()[0][1];
      vorname = vorname.substring(0, 3);
      nachname = nachname.substring(0, 3);
      vorname = vorname.toLowerCase();
      nachname = nachname.toLowerCase();
      String username = vorname + nachname + Integer.toString(uID);
      dbConnector.executeStatement("UPDATE nutzer SET username = '"+username+"' WHERE uID ='"+uID+"';");
    }
  
 
  
  public void schuelerLoeschen(int pID) {
      dbConnector.executeStatement("DELETE FROM nutzer WHERE uID = '"+pID+"';");
      dbConnector.executeStatement("DELETE FROM konto WHERE uID = '"+pID+"';");
  }
  
  public String getName() {
      return vorname + " " + name;
  }
  
  public ArrayList<String> getSchueler() {
      ArrayList<String> schueler = new ArrayList();
      dbConnector.executeStatement("SELECT uID, vorname, name FROM nutzer WHERE Rolle LIKE 'Schüler' ORDER BY uID ASC");
      QueryResult qr = dbConnector.getCurrentQueryResult();
      for(int x = 0; x < qr.getRowCount(); x++) {
          for(int y = 0; y < qr.getColumnCount(); y++) {
              schueler.add(qr.getData()[x][y]);
          }
      }

      return schueler;
  }
  // Ende Methoden

}
