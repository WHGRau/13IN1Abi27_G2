import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Nutzer extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private String uID;
  private String vorname;
  private String name;
  private String passwort;
  
  // Ende Attribute
  
  public Nutzer(String pID, String pVorname, String pName, String pPasswort) {
    // Frame-Initialisierung
    super("Mottoverwaltung");
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


  // Ende Methoden
}
