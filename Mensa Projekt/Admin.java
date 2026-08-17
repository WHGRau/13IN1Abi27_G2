import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Admin extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  
  // Ende Attribute
  
  public Admin() {
    // Frame-Initialisierung
    super("Mottoverwaltung");
    
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

  
  
  
  public void schuelerHinzufuegen(String pVorname, String pName, String pPasswort) {
      dbConnector.executeStatement("INSERT INTO nutzer(vorname, name, passwort, rolle) VALUES('"+pVorname+"','"+pName+"','"+pPasswort+"','Schüler')");
  }
  
  public void schuelerLoeschen(int pID) {
      dbConnector.executeStatement("DELETE FROM nutzer WHERE uID = '"+pID+"';");
  }
  // Ende Methoden
  
  public static void main(String[] args) {
    new Admin();
  }
}
